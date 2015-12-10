/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.service.schedule;

import com.github.rmannibucau.scheedule.service.data.CronRepository;
import com.github.rmannibucau.scheedule.service.jpa.Cron;
import com.github.rmannibucau.scheedule.service.jpa.ExecutionEvent;
import com.github.rmannibucau.scheedule.service.schedule.event.After;
import com.github.rmannibucau.scheedule.service.schedule.event.Before;
import lombok.extern.java.Log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import static javax.ejb.LockType.READ;
import static javax.ejb.TransactionAttributeType.SUPPORTS;

@Log
@Startup
@Singleton
@Lock(READ)
@TransactionAttribute(SUPPORTS)
public class Scheduler {
    @Resource
    private TimerService timerService;

    @Inject
    private CronRepository repository;

    @Inject
    private TaskFactory executor;

    @Inject
    private ExpressionParser parser;

    @Inject
    private Event<Before> beforeEvent;

    @Inject
    private Event<After> afterEvent;

    @Inject
    private ContextualProducer contextualProducer;

    private final Map<Long, Task> tasks = new HashMap<>();

    @PostConstruct
    private void redeploy() {
        repository.findByState(Cron.State.ON)
                .stream()
                .forEach(this::schedule);
    }

    public void schedule(final Cron cron) {
        final ScheduleExpression parse = parser.parse(cron.getCronExpression());
        if (parse == null) { // never, just a runnable task
            return;
        }

        tasks.put(
            cron.getId(),
            new Task(
                timerService.createCalendarTimer(parse, new TimerConfig(cron.getId(), false)),
                createRunnable(cron.getTask())));
    }

    private Runnable createRunnable(final String task) {
        final Runnable runnable = executor.create(task);
        return () -> {
            final ExecutionEvent ee = new ExecutionEvent();
            ee.setTask(task);

            final Date start = new Date();
            ee.setDate(start);

            beforeEvent.fire(new Before(ee));

            Throwable ex = null;
            try {
                runnable.run();
            } catch (final Throwable error) {
                ex = error;
            } finally {

                ee.setDuration(System.currentTimeMillis() - start.getTime());
                if (ex != null) {
                    final ByteArrayOutputStream writer = new ByteArrayOutputStream();
                    ex.printStackTrace(new PrintStream(writer));
                    ee.setExceptionString(new String(writer.toByteArray()));
                }

                afterEvent.fire(new After(ee));
            }
        };
    }

    public void unschedule(final Cron cron) {
        Optional.ofNullable(tasks.remove(cron.getId())).ifPresent(Task::cancel);
    }

    @PreDestroy
    private void cancel() {
        tasks.values().stream().forEach(Task::cancel);
        tasks.clear();
    }

    @Timeout
    public void executeTask(final Timer timer) {
        final Serializable info = timer.getInfo();
        if (info != null && Long.class.isInstance(info)) {
            final Long key = Long.class.cast(info);
            final Task task = tasks.get(key);
            if (task != null) {
                task.run();
            } else {
                log.warning("Can't find task '" + key + "'.");
            }
        } else {
            log.warning("Task " + info + " not runnable.");
        }
    }

    public void execute(final long id) {
        Task value = tasks.get(id);
        Optional.ofNullable(
                Optional.ofNullable(value).map(Task::getInstance).orElseGet(() ->
                    Optional.ofNullable(repository.findBy(id))
                        .map(Cron::getTask)
                        .map(this::createRunnable)
                        .orElse(null)))
                .ifPresent(Runnable::run);
    }
}
