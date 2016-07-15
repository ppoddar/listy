package com.listy.orm;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.listy.schema.Column;
import com.listy.schema.ForeignKey;
import com.listy.schema.Table;

import android.database.Cursor;

/**
 * Maps a Type or Java Class to a Database Table.
 * The mapping works with multiple {@link FieldMapping field-level } mappings
 * each for a persistent property of the type.
 * Most of the property values are stored on a columns of a same table,
 * but some property values (such as collection elements) can be stored
 * in a separate table.
 *
 * Created by ppoddar on 7/14/16.
 */
public interface TypeMapping extends Iterable<FieldMapping> {
    /**
     * Gets the field-level mappings.
     * @return a set of field-level mappings. Never null.
     */

    Collection<FieldMapping> getMappings();

    /**
     * Gets the field mapping for the given property.
     * @param property
     * @return
     */
    FieldMapping getMapping(String property);

    /**
     * Gets the  type of object being mapped.
     * @return a class never null
     */
    public  Class<?> getObjectType();

    /**
     * Gets the table where the basic field values are stored.
     * Some field values such as elements of a collection may be stored on other table.
     *
     *
     * @return
     */
    Table getTable();

    /**
     * Gets the tables for those field mapping not owned by this mapping.
     * Some field mappings that store elements of a collection belong to this mapping
     * but is not owned by it.
     *
     *
     * @return
     */
    Set<Table> getChildTables();

    /**
     * Gets the mapping that stores date to the given child table.
     * @return
     */
    FieldMapping getMappingToTable(Table t);

    /**
     * Returns the SQL to create schema for target table
     * @return
     */
    SQL defineSchemaSQL();

    /**
     * Returns the SQL to drop schema for target table
     * @return
     */

    SQL dropSchemaSQL();

    /**
     * Populate the given instance and its owned instances from Cursor data
     * @param target
     * @param cursor
     */
    void populate(Object target, Cursor cursor);

    /**
     * Create zero or more SQL to store the given object and all other owned objects.
     * An object x owns another object y is decided by the mapping assocaited with the
     * property y
     *
     * @param target
     * @param ctx
     * @return
     */
    List<SQL> insertSQL(Object target, ORMContext ctx);


    /**
     * Adds a basic mapping
     * @param property name of the instance property. This {@link #objectType()} type}
     *                 must have the property.
     * @param column the target column. The column must belong to the
     *                   {@link #getTable()} owning} table.
     */
    void addBasicMapping(String property, Column column);

    /**
     * Adds a mapping for primary key property. The target table must have a primary
     * key column {@link Table#getPrimaryKeyColumn()}
     * @param pkProperty name of the instance property that designates the primary key.
     *                   This {@link #objectType()} type}
     *                 must have the property.
     *
     */
    void addPrimaryKeyMapping(String pkProperty);

    /**
     * Adds a mapping for collection property. The target table may not have a column
     * to store collection elements but the collection elements may be stored in a
     * different table.
     * @param property name of a property designating a collection value
     * @param fk the database foreign key that represent the relation
     */
    void addCollectionMapping(String property, ForeignKey fk, TypeMapping elementTypeMapping);

    void addElementMapping(String property, ForeignKey fk);



}
