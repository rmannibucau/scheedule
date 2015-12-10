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

import com.github.rmannibucau.scheedule.resource.domain.Event;
import com.github.rmannibucau.scheedule.resource.domain.Page;
import com.github.rmannibucau.scheedule.service.data.EventRepository;
import com.github.rmannibucau.scheedule.service.jpa.ExecutionEvent;
import org.apache.deltaspike.data.api.QueryResult;

import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static javax.ejb.LockType.READ;

@Path("event")
@Singleton
@Lock(READ)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {
    @Inject
    private EventRepository repository;

    @GET
    public Page<Event> all(@QueryParam("first") @DefaultValue("0") final int first,
                              @QueryParam("max") @DefaultValue("50") final int max) {
        final QueryResult<ExecutionEvent> all = repository.all(first, max);
        return new Page<>(all.getResultList().stream().map(this::toEvent).collect(toList()), all.count());
    }

    @GET
    @Path("{id}")
    public Event get(@PathParam("id") final long id) {
        return Optional.ofNullable(repository.findBy(id)).map(this::toEvent).orElse(null);
    }

    private Event toEvent(final ExecutionEvent e) {
        return new Event(e.getId(), new Date(e.getDate().getTime()) /* jpa -> normal */, e.getExceptionString(), e.getTask(), e.getDuration());
    }
}
