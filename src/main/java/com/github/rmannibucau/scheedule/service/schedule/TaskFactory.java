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

import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.Set;

@ApplicationScoped
public class TaskFactory {
    @Inject
    private BeanManager bm;

    public Runnable create(final String task) {
        final Bean<?> bean;
        final Class<?> recipeClass;
        try {
            Set<Bean<?>> beans = bm.getBeans(task);
            if (beans == null || beans.isEmpty()) {
                recipeClass = Thread.currentThread().getContextClassLoader().loadClass(task.trim());
                beans = bm.getBeans(recipeClass);
            }
            bean = bm.resolve(beans);
            if (bean == null) {
                throw new IllegalArgumentException("[" + task + "] " +"Can't find a matching bean.");
            }
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("[" + task + "] " +"Can't create task: " + e.getMessage());
        }
        return bm.isNormalScope(bean.getScope()) /* @AppScoped or custom, other std normal scopes would be ~broken */ ?
                Runnable.class.cast(bm.getReference(bean, Runnable.class, bm.createCreationalContext(null))) :
                new NotNormalScopedRunnable(bm, bean);
    }

    @RequiredArgsConstructor
    private static class NotNormalScopedRunnable implements Runnable {
        private final BeanManager bm;
        private final Bean<?> bean;

        @Override
        public void run() {
            final CreationalContext<Object> creationalContext = bm.createCreationalContext(null);
            try {
                Runnable.class.cast(bm.getReference(bean, Runnable.class, creationalContext)).run();
            } finally {
                creationalContext.release();
            }
        }
    }
}
