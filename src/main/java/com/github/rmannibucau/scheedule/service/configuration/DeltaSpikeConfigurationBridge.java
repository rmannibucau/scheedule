/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.service.configuration;

import com.github.rmannibucau.scheedule.service.data.ConfigurationRepository;
import com.github.rmannibucau.scheedule.service.jpa.Property;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.spi.config.ConfigSource;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static javax.ejb.LockType.READ;

@Startup // ensure it is done at startup otherwise tasks can be created before we added our config
@Singleton
@Lock(READ)
public class DeltaSpikeConfigurationBridge implements ConfigSource {
    public interface Keys { // some application wide keys
        String WORK_DIR = "tribe.cron.workDir";
    }

    @Inject
    private ConfigurationRepository repository;

    // auto adjustement value for built-in keys
    private String workDir;

    @PostConstruct
    private void init() {
        workDir = Optional.ofNullable(repository.findBy(Keys.WORK_DIR))
                .map(Property::getValue)
                .orElse(
                    Optional.of(new File(System.getProperty("openejb.base", "."), "work"))
                            .filter(File::isDirectory)
                            .map(f -> {
                                final File dir = new File(f, "tribe-cron.");
                                dir.mkdirs();
                                return f;
                            })
                            .map(File::getAbsolutePath)
                            .orElse(System.getProperty("java.io.tmpdir")));

        ConfigResolver.addConfigSources(singletonList(this));
    }

    @Override
    public int getOrdinal() {
        return 1000;
    }

    @Override
    public String getPropertyValue(final String key) {
        return Optional.ofNullable(repository.findBy(key))
                .map(Property::getValue)
                .orElseGet(()-> Keys.WORK_DIR.equals(key) ? workDir : null);
    }

    @Override
    public String getConfigName() {
        return "scheedule";
    }

    @Override
    public boolean isScannable() {
        return false;
    }

    @Override
    public Map<String, String> getProperties() {
        return emptyMap();
    }
}
