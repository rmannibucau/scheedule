/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.packaging;

import com.github.rmannibucau.scheedule.task.SimpleTask;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.impl.config.ConfigurationExtension;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.impl.RepositoryExtension;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;
import org.apache.deltaspike.jpa.impl.transaction.TransactionalInterceptor;
import org.apache.deltaspike.partialbean.api.PartialBeanBinding;
import org.apache.deltaspike.partialbean.impl.PartialBeanBindingExtension;
import org.apache.deltaspike.proxy.api.DeltaSpikeProxyFactory;
import org.apache.deltaspike.proxy.impl.AsmProxyClassGenerator;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.io.File;

import static org.apache.ziplock.JarLocation.jarLocation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Packaging {
    public static WebArchive application() {
        final File webInf = new File(jarLocation(Packaging.class).getParentFile().getParentFile(), "src/main/webapp/WEB-INF/");
        return ShrinkWrap.create(WebArchive.class, "scheedule.war")
                .addPackages(true, "com.github.rmannibucau.scheedule.service")
                .addPackages(true, "com.github.rmannibucau.scheedule.api")
                .addClasses(SimpleTask.class)
                .addAsWebInfResource(new FileAsset(new File(webInf, "beans.xml")), "beans.xml")
                .addAsWebInfResource(new FileAsset(new File(webInf, "persistence.xml")), "persistence.xml")
                .addAsWebInfResource(new StringAsset(
                        "<resources>" +
                        "<Resource id=\"jdbc/cron\" type=\"DataSource\">\n" +
                        "JdbcUrl = jdbc:hsqldb:mem:crontest\n" +
                        "</Resource>" +
                        "</resources>"), "resources.xml")
                .addAsLibraries( // not using maven cause our maven deps breaks SW maven
                        // deltaspike, not using SW maven cause we use maven (conflict)
                        jarLocation(Repository.class),
                        jarLocation(RepositoryExtension.class),
                        jarLocation(ConfigProperty.class),
                        jarLocation(ConfigurationExtension.class),
                        jarLocation(PartialBeanBinding.class),
                        jarLocation(PartialBeanBindingExtension.class),
                        jarLocation(DeltaSpikeProxyFactory.class),
                        jarLocation(AsmProxyClassGenerator.class),
                        jarLocation(TransactionScoped.class),
                        jarLocation(TransactionalInterceptor.class));
    }
}
