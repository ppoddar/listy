package com.listy.domain;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Base of all persistent object.
 *
 */
public abstract class DomainObject {
    private static AtomicLong idCounter = new AtomicLong(System.currentTimeMillis());
    protected final long id;

    protected DomainObject() {
        id = idCounter.incrementAndGet();
    }

    public final long getId() {
        return id;
    }
    /**
     * Gets the value for a given column
     * @param colName
     * @return
     */
    public abstract Object getValue(String colName);
}
