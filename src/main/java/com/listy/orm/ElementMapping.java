package com.listy.orm;

import com.listy.schema.Column;
import com.listy.schema.ForeignKey;

import android.database.Cursor;

/**
 * Maps the collection elements to a table. The table has a foreign key
 * to another table which represents the collection (but may not have the data)
 *
 * Created by ppoddar on 7/14/16.
 */
public class ElementMapping implements FieldMapping  {
    private TypeMapping owner;
    private ForeignKey fk;
    
    public ElementMapping(TypeMapping owner, ForeignKey fk) {
        this.owner = owner;
        this.fk = fk;
    }

    /**
     * Must not be called. There is no property associated with this mapping
     */
    @Override
    public String getProperty() {
        throw new AbstractMethodError();
    }

    @Override
    public TypeMapping getOwner() {
        return owner;
    }
    
    @Override
    public Column getColumn() {
        return fk;
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

    }

    /**
     * Return SQL fragment to insert
     *
     * @param buffer
     * @param target    object being inserted
     * @param container object that contains or embeds the target
     *                  optional. required for foreign key resolution
     * @param ctx
     * @return
     */
    @Override
    public void insert(SQL buffer, Object target, Object elem, ORMContext ctx) {
        buffer.appendColumnValue(fk.name(), extractValueFrom(elem, ctx));
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
        return new StringBuilder().append(fk.name()).append(" ").append(fk.type());
    }

    @Override
    public StringBuilder defineConstraint() {

        return new StringBuilder().append("FOREIGN KEY ")
                .append('(').append(fk.name()).append(')')
                .append(" REFERENCES ")
                .append(fk.targetColumn().table().name())
                .append('(').append(fk.targetColumn().name()).append(')');

    }

    @Override
    public boolean isBasic() {
        return false;
    }

    /**
     * Extract the value to be stored from the given object.
     *
     * @param object
     * @param ctx
     * @return
     */
    @Override
    public Object extractValueFrom(Object element, ORMContext ctx) {
        return ctx.primaryKey(element);
    }
}
