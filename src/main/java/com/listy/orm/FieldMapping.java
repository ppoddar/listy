package com.listy.orm;

import android.database.Cursor;

import com.listy.schema.Column;
import com.listy.schema.Table;

/**
 * Created by ppoddar on 7/14/16.
 */
public interface FieldMapping {
    /**
     * Gets the name of the instance field  being stored.
     * This value can be null for the mapping that stores element values of a collection.
     * @return
     */
    String getProperty();
    
    Column getColumn();

    /**
     */
    TypeMapping getOwner();


    /**
     * Populate given target from cursor data at current position.
     * Do not move the cursor.
     * @param target
     * @param cursor
     */
    void populate(Object target, Cursor cursor);

    /**
     * Return SQL fragment to insert
     *
     * @param buffer
     * @param target object being inserted
     * @param container object that contains or embeds the target
     *                  optional. required for foreign key resultion
     * @return
     */
    void insert(SQL buffer, Object target, Object container, ORMContext ctx);
    void update(SQL buffer);
    void delete(SQL buffer);
    void query(SQL buffer);

    StringBuilder defineSchema();
    StringBuilder defineConstraint();

    boolean isBasic();


    /**
     * Extract the value to be stored from the given object.
     *
     * @param obj
     * @param ctx
     * @return
     */
    Object extractValueFrom(Object object, ORMContext ctx);


}
