package com.listy.orm;

import java.util.List;

import com.listy.schema.Table;

import android.content.ContentValues;

/**
 * Created by ppoddar on 7/14/16.
 */
public interface ORMContext {
    SQL createSchemaDefinitionSQL(Class<?> cls);
    SQL createDropTableSQL(Class<?> cls);
    List<SQL> createInsertSQL(Object instance);
    ContentValues getContentValues(Object object);
    Table getTable(Class<?> cls);
    Object primaryKey(Object obj);
    
    void addMapping(Class<?> cls, TypeMapping mapping);
}
