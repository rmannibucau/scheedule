/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.github.rmannibucau.scheedule.recipe;

import javax.inject.Named;

@Named
public class SampleTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Executed");
    }
}
