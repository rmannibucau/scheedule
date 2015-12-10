/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.task;

import com.github.rmannibucau.scheedule.api.Event;
import com.github.rmannibucau.scheedule.service.jpa.ExecutionEvent;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class EventAwareTask implements Runnable {
    @Event
    @Inject
    private ExecutionEvent event; // real impl can use Provider<ExecutionEvent> if the task is normal scoped

    private static ExecutionEvent firstEvent;

    @Override
    public void run() {
        synchronized (EventAwareTask.class) {
            if (firstEvent != null) {
                return;
            }
            firstEvent = event;
        }
    }

    public static ExecutionEvent first() {
        return firstEvent;
    }
}
