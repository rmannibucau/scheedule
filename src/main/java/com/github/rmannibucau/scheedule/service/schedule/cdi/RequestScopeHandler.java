/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.service.schedule.cdi;

import com.github.rmannibucau.scheedule.service.schedule.event.After;
import com.github.rmannibucau.scheedule.service.schedule.event.Before;
import lombok.Getter;
import lombok.Setter;
import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContextsService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class RequestScopeHandler {
    private ContextsService owb;

    @Inject
    private DataHolder holder;

    @PostConstruct
    private void init() {
        try {
            owb = WebBeansContext.currentInstance().getContextsService();
        } catch (final Throwable th) {
            // no-op
        }
    }

    public void before(@Observes final Before ignored) {
        if (owb == null) {
            return;
        }

        final boolean noRequestScope = owb.getCurrentContext(RequestScoped.class) == null;
        if (noRequestScope) {
            owb.startContext(RequestScoped.class, null);
        }
        holder.setNeedDestroy(noRequestScope);
    }

    public void after(@Observes final After ignored) {
        if (owb == null) {
            return;
        }

        if (holder.isNeedDestroy()) {
            owb.endContext(RequestScoped.class, null);
        }
    }

    @Getter @Setter
    private static class DataHolder {
        private boolean needDestroy;
    }
}
