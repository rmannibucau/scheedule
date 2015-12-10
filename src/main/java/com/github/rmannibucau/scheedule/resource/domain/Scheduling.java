/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.resource.domain;

import lombok.Data;

@Data
public class Scheduling {
    private long id;
    private String name;
    private String cron;
    private String task;
    private boolean state;
}
