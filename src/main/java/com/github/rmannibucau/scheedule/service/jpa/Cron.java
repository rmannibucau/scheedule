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
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter @Setter
@Table(name = "crons")
@NamedQueries({
        @NamedQuery(name = "Cron.findAll", query = "select c from Cron c"),
        @NamedQuery(name = "Cron.findByState", query = "select c from Cron c where c.state = :state")
})
public class Cron {
    @Id
    @GeneratedValue // TODO table generator
    private long id;

    @Column(name = "scheduling_name", nullable = false, unique = true)
    private String schedulingName;

    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;

    @Column(nullable = false)
    private String task;

    @Column(nullable = false)
    @Enumerated(STRING)
    private State state;

    public enum State {
        ON, OFF
    }
}
