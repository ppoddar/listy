package com.listy.schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.listy.orm.FieldMapping;
import com.listy.orm.ORMContext;
import com.listy.orm.SQL;
import com.listy.orm.TypeMapping;

import android.content.ContentValues;

/**
 * ORM Mini or Mobile
 *
 * Created by ppoddar on 7/13/16.
 */
public abstract class DefaultORMContext implements ORMContext {
    private Map<Class<?>, TypeMapping> _mappings =
            new HashMap<Class<?>, TypeMapping>();

    public void addMapping(Class<?> cls, TypeMapping mapping) {
        _mappings.put(cls, mapping);
    }

    /**
     * Get table name of the given class.
     *
     * @param cls
     * @return
     */
    public TypeMapping getMapping(Class<?> cls) {
        if (!_mappings.containsKey(cls)) {
            throw new RuntimeException("no mapping for " + cls);
        }
        return _mappings.get(cls);
    }

    /**
     * Gets the table where the given instance is stored
     * @param cls
     * @return
     */
    public Table getTable(Class<?> cls) {
        return getMapping(cls).getTable();
    }


    public SQL createDropTableSQL(Class<?> cls) {
        TypeMapping mapping = getMapping(cls);
        return mapping.dropSchemaSQL();
    }

    public SQL createSchemaDefinitionSQL(Class<?> cls) {
        TypeMapping mapping = getMapping(cls);
        return mapping.defineSchemaSQL();
    }

    public List<SQL> createInsertSQL(Object object) {
        TypeMapping mapping = getMapping(object.getClass());
        return mapping.insertSQL(object, this);
    }


    public ContentValues getContentValues(Object object) {
        ContentValues values = new ContentValues();
        TypeMapping typeMapping = getMapping(object.getClass());
        for (FieldMapping mapping : typeMapping) {
            Object value = mapping.extractValueFrom(object, this); // this is the tricky part
            if (value == null) continue;
            if (String.class.isInstance(value)) {
                values.put(mapping.getColumn().name(), String.class.cast(value));
            } else if (Integer.class.isInstance(value)) {
                values.put(mapping.getColumn().name(), Integer.class.cast(value));
            } else if (Long.class.isInstance(value)) {
                values.put(mapping.getColumn().name(), Long.class.cast(value));
            } else {
                throw new RuntimeException("Unsupported value ["
                        + value + "] (" + value.getClass() + ") for " + mapping.getColumn().name());
            }
        }
        return values;
    }

    public static String getPrimitiveColumnType(Class<?> javaClass) {
        if (Number.class.isAssignableFrom(javaClass)) return "NUM";
        return "TEXT";
    }
}
