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

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@Table(name = "execution_events")
@NamedQueries({
    @NamedQuery(name = "ExecutionEvent.findAll", query = "select e from ExecutionEvent e order by e.date desc")
})
public class ExecutionEvent {
    @Id
    @GeneratedValue // TODO table generator
    private long id;

    @Temporal(TIMESTAMP)
    private Date date;

    @Lob
    private String exceptionString;

    private String task;
    private long duration;
}
