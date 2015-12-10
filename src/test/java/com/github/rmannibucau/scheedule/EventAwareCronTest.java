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
import com.github.rmannibucau.scheedule.task.EventAwareTask;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class EventAwareCronTest {
    @Deployment
    public static Archive<?> app() {
        return Packaging.application()
            .addClasses(EventAwareTask.class)
            .addAsWebInfResource(new StringAsset(
                    "insert into crons (id, scheduling_name, cron_expression, task, state)" +
                    "values (2, 'EventAwareCronTest', '* * * * * *', '" + EventAwareTask.class.getName() + "', 'ON')"),
                    "classes/import-cron.sql");
    }

    @Test
    public void run() {
        final long current = System.currentTimeMillis();
        while (System.currentTimeMillis() - current < TimeUnit.MINUTES.toMillis(5) && EventAwareTask.first() == null) {
            try {
                sleep(500);
            } catch (final InterruptedException e) {
                Thread.interrupted();
                return;
            }
        }
        assertNotNull(EventAwareTask.first());
        assertNotNull(EventAwareTask.first().getDate());
        assertNotNull(EventAwareTask.first().getTask());
        try { // no luck if a noop takes more than 500ms, that's just a guard to ensure we have not a concurrency issue but shouldn't happen
            sleep(500);
        } catch (final InterruptedException e) {
            Thread.interrupted();
            return;
        }
        assertNotNull(EventAwareTask.first().getDuration());
    }
}
