/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule;

import com.github.rmannibucau.scheedule.packaging.Packaging;
import com.github.rmannibucau.scheedule.task.SimpleTask;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class CronTest {
    @Deployment
    public static Archive<?> app() {
        return Packaging.application()
                .addAsWebInfResource(new StringAsset(
                        "insert into crons (id, scheduling_name, cron_expression, task, state) values (1, 'test', '* * * * * *', '" + SimpleTask.class.getName() + "', 'ON')"),
                        "classes/import-cron.sql");
    }

    @Inject
    private SimpleTask task;

    @Test
    public void run() {
        final int count = task.getCount();
        final long current = System.currentTimeMillis();
        while (System.currentTimeMillis() - current < TimeUnit.MINUTES.toMillis(5) && count == task.getCount()) {
            try {
                sleep(500);
            } catch (final InterruptedException e) {
                Thread.interrupted();
                return;
            }
        }
        assertTrue(count < task.getCount());
    }
}
