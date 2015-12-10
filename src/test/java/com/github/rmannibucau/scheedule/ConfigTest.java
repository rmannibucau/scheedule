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

import com.github.rmannibucau.scheedule.service.data.ConfigurationRepository;
import com.github.rmannibucau.scheedule.service.jpa.Property;
import com.github.rmannibucau.scheedule.packaging.Packaging;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.api.provider.DependentProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.Singleton;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class ConfigTest {
    @Deployment
    public static Archive<?> app() {
        return Packaging.application();
    }

    @Inject
    private ConfigurationRepository repository;

    @Inject
    private Tx tx;

    @Test
    public void run() {
        assertNull(myconfig());

        final Property entity = new Property();
        entity.setKey("myconfig");
        entity.setValue("expected");
        tx.exec(() -> repository.saveAndFlush(entity));

        assertEquals("expected", myconfig());
        tx.exec(() -> repository.remove(repository.findBy(entity.getKey())));
    }

    public Object myconfig() {
        final DependentProvider<LazyBean> dependent = BeanProvider.getDependent(LazyBean.class);
        try {
            return dependent.get().getMyConfig();
        } finally {
            dependent.destroy();
        }
    }

    public static class LazyBean {
        @Inject
        @ConfigProperty(name = "myconfig")
        private String myConfig;

        public String getMyConfig() {
            return myConfig;
        }
    }

    @Singleton
    public static class Tx {
        public void exec(final Runnable task) {
            task.run();
        }
    }
}
