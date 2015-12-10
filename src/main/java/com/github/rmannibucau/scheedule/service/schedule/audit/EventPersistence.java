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

import com.github.rmannibucau.scheedule.service.data.EventRepository;
import com.github.rmannibucau.scheedule.service.schedule.event.After;

import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static javax.ejb.LockType.READ;

@Singleton
@Lock(READ)
public class EventPersistence {
    @Inject
    private EventRepository repository;

    @Asynchronous // better to let it failing than breaking main execution
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void listen(@Observes final After ee) {
        repository.saveAndFlush(ee.getExecutionEvent());
    }
}
