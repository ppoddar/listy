package com.listy.orm;

import com.listy.schema.Column;

import android.database.Cursor;

/**
 * Created by ppoddar on 7/14/16.
 */
public class FieldMappingAdapter implements FieldMapping {
    /**
     * Gets the name of the instance field  being stored.
     * This value can be null for the mapping that stores element values of a collection.
     *
     * @return
     */
    @Override
    public String getProperty() {
        throw new AbstractMethodError();
    }

    /**
     */
    @Override
    public TypeMapping getOwner() {
        throw new AbstractMethodError();
    }

    /**
     * Populate given target from cursor data at current position.
     * Do not move the cursor.
     *
     * @param target
     * @param cursor
     */
    @Override
    public void populate(Object target, Cursor cursor) {
        throw new AbstractMethodError();

    }

    /**
     * Return SQL fragment to insert
     *
     * @param buffer
     * @param target    object being inserted
     * @param container object that contains or embeds the target
     *                  optional. required for foreign key resultion
     * @param ctx
     * @return
     */
    @Override
    public void insert(SQL buffer, Object target, Object container, ORMContext ctx) {
        throw new AbstractMethodError();

    }

    @Override
    public void update(SQL buffer) {
        throw new AbstractMethodError();

    }

    @Override
    public void delete(SQL buffer) {
        throw new AbstractMethodError();

    }

    @Override
    public void query(SQL buffer) {
        throw new AbstractMethodError();

    }

    @Override
    public StringBuilder defineSchema() {
        throw new AbstractMethodError();
    }

    @Override
    public StringBuilder defineConstraint() {
        throw new AbstractMethodError();
    }

    @Override
    public boolean isBasic() {
        throw new AbstractMethodError();
    }

    /**
     * Extract the value to be stored from the given object.
     *
     * @param object
     * @param ctx
     * @return
     */
    @Override
    public Object extractValueFrom(Object object, ORMContext ctx) {
        throw new AbstractMethodError();
    }

    @Override
    public Column getColumn() {
        throw new AbstractMethodError();
    }
}
