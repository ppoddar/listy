package com.listy.orm;

import com.listy.schema.Column;
import com.listy.schema.MORM;
import com.listy.schema.Table;

import java.lang.reflect.Method;

/**
 * Created by ppoddar on 7/14/16.
 */
public class PrimaryKeyFieldMapping extends BasicFieldMapping {

    public PrimaryKeyFieldMapping(TypeMapping owner, String pkProperty, Table t) {
        this(owner, pkProperty, t.getPrimaryKeyColumn());
    }

        /**
         * Supply the class whose primary key field is to be mapped
         * @param cls
         */
    public PrimaryKeyFieldMapping(TypeMapping owner, String pkProperty, Column pkColumn) {
        super(owner, owner.getObjectType(), pkProperty, pkColumn);
        try {
            getter = Reflection.getGetter(owner.getObjectType(), pkProperty);
            if (getter == null) {
                throw new RuntimeException("No getter for primary field in " + owner.getObjectType());
            }
            setter = null;
            col = pkColumn;
            initMapping(getter, setter, col);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public StringBuilder defineConstraint() {
        return new StringBuilder("PRIMARY KEY(").append(col.name()).append(")");
    }


}
