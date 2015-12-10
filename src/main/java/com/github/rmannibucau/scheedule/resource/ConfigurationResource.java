/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.resource;

import com.github.rmannibucau.scheedule.resource.domain.KeyValue;
import com.github.rmannibucau.scheedule.resource.domain.Page;
import com.github.rmannibucau.scheedule.service.data.ConfigurationRepository;
import com.github.rmannibucau.scheedule.service.jpa.Property;

import java.util.List;
import java.util.Optional;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static java.util.stream.Collectors.toList;
import static javax.ejb.LockType.READ;

@Singleton
@Lock(READ)
@Path("configuration")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
    @Inject
    private ConfigurationRepository repository;

    @GET
    public Page<KeyValue> all(@QueryParam("first") @DefaultValue("0") final int first,
                              @QueryParam("max") @DefaultValue("50") final int max) {
        final List<Property> queryResult = repository.all(first, max);
        return new Page<>(queryResult.stream().map(this::toKeyValue).collect(toList()), repository.count());
    }

    @GET
    @Path("{id}")
    public KeyValue get(@PathParam("id") final String id) {
        return Optional.ofNullable(repository.findBy(id)).map(this::toKeyValue).orElse(null);
    }

    @POST
    public KeyValue post(final KeyValue value) {
        final Property property = new Property();
        property.setKey(value.getKey());
        property.setValue(value.getValue());
        repository.saveAndFlush(property);
        return value;
    }

    @PUT
    @Path("{id}")
    public KeyValue put(@PathParam("id") final String key, final KeyValue value) {
        Optional.ofNullable(repository.findBy(key)).ifPresent(config -> {
            config.setValue(value.getValue());
            repository.flush();
        });
        return value;
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") final String key) {
        repository.remove(repository.findBy(key));
    }

    private KeyValue toKeyValue(final Property property) {
        return new KeyValue(property.getKey(), property.getValue());
    }
}
