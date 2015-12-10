/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.service.schedule.audit;

import com.github.rmannibucau.scheedule.service.jpa.ExecutionEvent;
import com.github.rmannibucau.scheedule.service.schedule.event.After;
import lombok.extern.java.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@Log(topic = "tribe.cron.execution")
@ApplicationScoped
public class EventLogging {
    public void listen(@Observes final After after) {
        final ExecutionEvent ee = after.getExecutionEvent();
        if (ee.getExceptionString() == null) {
            log.info(ee.getTask() + " was executed in " + ee.getDuration() + "ms.");
        } else {
            log.severe(ee.getTask() + " was executed in " + ee.getDuration() + "ms\n" + ee.getExceptionString());
        }
    }
}
