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

import com.github.rmannibucau.scheedule.api.Event;
import com.github.rmannibucau.scheedule.service.jpa.ExecutionEvent;
import com.github.rmannibucau.scheedule.service.schedule.event.Before;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

@RequestScoped
public class ContextualProducer { // allow injection in run() if the ExecutionEvent, note the id is not yet set so can be used by relationship only
    private ExecutionEvent executionEvent;

    public void before(@Observes final Before before) {
        this.executionEvent = before.getExecutionEvent();
    }

    @Event
    @Produces
    public ExecutionEvent event() {
        return executionEvent;
    }
}
