/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.resource;

import com.github.rmannibucau.scheedule.resource.domain.Page;
import com.github.rmannibucau.scheedule.resource.domain.Scheduling;
import com.github.rmannibucau.scheedule.service.data.CronRepository;
import com.github.rmannibucau.scheedule.service.jpa.Cron;
import com.github.rmannibucau.scheedule.service.schedule.Scheduler;
import org.apache.deltaspike.data.api.QueryResult;

import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static javax.ejb.LockType.READ;

@Path("cron")
@Singleton
@Lock(READ)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CronResource {
    @Inject
    private Scheduler scheduler;

    @Inject
    private CronRepository repository;

    @HEAD
    @Path("force/{id}")
    public void forceExecution(@PathParam("id") final long id) {
        scheduler.execute(id);
    }

    @GET
    public Page<Scheduling> get(@QueryParam("first") @DefaultValue("0") final int first,
                                @QueryParam("max") @DefaultValue("50") final int max) {
        final QueryResult<Cron> result = repository.all(first, max);
        return new Page<>(
                result.getResultList()
                    .stream()
                    .map(this::toScheduling)
                    .collect(toList()),
                result.count());
    }

    @GET
    @Path("{id}")
    public Scheduling get(@PathParam("id") final long id) {
        return Optional.ofNullable(repository.findBy(id)).map(this::toScheduling).orElse(null);
    }

    @POST
    public Scheduling post(final Scheduling value) {
        final Cron cron = new Cron();
        cron.setSchedulingName(value.getName());
        cron.setCronExpression(value.getCron());
        cron.setTask(value.getTask());
        cron.setState(value.isState() ? Cron.State.ON : Cron.State.OFF);
        repository.saveAndFlush(cron);
        if (cron.getState() == Cron.State.ON) {
            scheduler.schedule(cron);
        }
        return value;
    }

    @PUT
    @Path("{id}")
    public Scheduling put(@PathParam("id") final long id, final Scheduling value) {
        Optional.ofNullable(repository.findBy(id)).ifPresent(cron -> {
            scheduler.unschedule(cron);
            cron.setState(value.isState() ? Cron.State.ON : Cron.State.OFF);
            cron.setTask(value.getTask());
            cron.setSchedulingName(value.getName());
            cron.setCronExpression(value.getCron());
            repository.flush();
            if (cron.getState() == Cron.State.ON) {
                scheduler.schedule(cron);
            }
        });
        return value;
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") final long id) {
        final Cron cron = repository.findBy(id);
        if (cron != null) {
            scheduler.unschedule(cron);
            repository.remove(cron);
        }
    }

    private Scheduling toScheduling(final Cron cron) {
        final Scheduling scheduling = new Scheduling();
        scheduling.setId(cron.getId());
        scheduling.setTask(cron.getTask());
        scheduling.setCron(cron.getCronExpression());
        scheduling.setName(cron.getSchedulingName());
        scheduling.setState(cron.getState() == Cron.State.ON);
        return scheduling;
    }
}
