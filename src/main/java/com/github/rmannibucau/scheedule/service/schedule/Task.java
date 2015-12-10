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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.ejb.Timer;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Task {
    private final Timer timer;
    private final Runnable instance;

    public void cancel() {
        timer.cancel();
    }

    public void run() {
        instance.run();
    }
}
