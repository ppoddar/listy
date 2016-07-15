package com.listy.schema;

import com.listy.util.Assertions;

/**
 * Created by ppoddar on 7/13/16.
 */
public class Column {
    private Table table;
    private String name;
    private String type;
    private String constraint;


    public Column(Table table, String name, String type) {
        Assertions.assertNotNull(table, "Column can not have a null table");
        Assertions.assertNotNull(name, "Column can not have a null name");
        Assertions.assertTrue(name.trim().length() != 0, "Column can not have an empty name");
        Assertions.assertNotNull(type, "Column can not have a null type");
        Assertions.assertTrue(type.trim().length() != 0, "Column can not have an empty type");
        this.table = table;
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public String fullName() {
        return table().name() + '.' + name();
    }

    public Table table() {
        return table;
    }
    public void setConstraint(String constraint) {
        this.constraint = constraint;

    }
    public String constraints() {
        return constraint;
    }

    public boolean hasConstraint() {
        return constraint == null || constraint.trim().length() == 0;
    }

    public String type() {
        return type;
    }

    public String toString() {
        return name() + ":" + type();
    }

}
