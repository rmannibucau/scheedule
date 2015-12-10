/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.service.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Getter @Setter
@NamedQueries({
    @NamedQuery(name = "Property.findAll", query = "select p from Property p order by p.key")
})
@Table(name = "properties")
public class Property {
    @Id
    @Column(name = "property_key")
    private String key;

    @Column(name = "property_value")
    private String value;
}
