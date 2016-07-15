package com.listy.orm;

import android.database.Cursor;

import com.listy.schema.Column;
import com.listy.schema.Table;
import com.listy.util.Assertions;

import java.lang.reflect.Method;

/**
 * Created by ppoddar on 7/14/16.
 */
public class BasicFieldMapping extends FieldMappingAdapter implements FieldMapping {
    protected TypeMapping owner;
    protected String property;
    protected Column col;
    protected Method getter;
    protected Method setter;

    private BasicFieldMapping() {

    }

    /**
     * @param owner    owner of this mapping
     * @param property instance field name.
     * @param col
     */
    public BasicFieldMapping(TypeMapping owner, Class cls, String property, Column col) {
        this(owner, cls, property, col, true);
    }


    protected BasicFieldMapping(TypeMapping owner, Class cls, String property, Column col, boolean initMapping) {
        this.owner = owner;
        this.property = property;
        if (initMapping) {
            initMapping(Reflection.getGetter(cls, property, true),
                    Reflection.getSetter(cls, property, false),
                    col);
        }

    }

    public BasicFieldMapping(Method getter, Method setter, Column col) {
        initMapping(getter, setter, col);
    }

    protected void initMapping(Method getter, Method setter, Column col) {
        this.col = col;
        this.getter = getter;
        this.setter = setter;
    }

    public String getProperty() {
        return property;
    }
    
    public Column getColumn() {
        return col;
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
        try {
            int i = cursor.getColumnIndexOrThrow(col.name());
            String value = cursor.getString(i);
            setter.invoke(target, value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isBasic() {
        return true;
    }


    /**
     * Return SQL fragment to insert
     *
     * @return
     */
    @Override
    public void insert(SQL buffer, Object target, Object container, ORMContext ctx) {
        buffer.appendColumnValue(col.name(), extractValueFrom(target, ctx));

    }

    @Override
    public void update(SQL buffer) {

    }

    @Override
    public void delete(SQL buffer) {

    }

    @Override
    public void query(SQL buffer) {

    }

    @Override
    public StringBuilder defineSchema() {
        return new StringBuilder().append(col.name()).append(" ").append(col.type());
    }

    @Override
    public StringBuilder defineConstraint() {
        return null;
    }


    @Override
    public TypeMapping getOwner() {
        return owner;
    }

    public Object extractValueFrom(Object obj, ORMContext ctx) {
        try {
            return getter.invoke(obj, Reflection.EMPTY_ARGS);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
