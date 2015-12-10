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

import java.util.Collection;

public class Page<T> {
    private long total;
    private Collection<T> data;

    public Page(final Collection<T> data, final long total) {
        this.data = data;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public Collection<T> getData() {
        return data;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public void setData(final Collection<T> data) {
        this.data = data;
    }
}
