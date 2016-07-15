package com.listy.schema;

import com.listy.util.Assertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ppoddar on 7/13/16.
 */
public class Table {
    private String name;
    private Map<String, Column> columns = new LinkedHashMap<String,Column>();
    private Column pkColumn;

    public Table(String name) {
        this.name = name;
    }

    public Column addPrimaryKeyColumn(Column col) {
        addColumn(col);
        pkColumn = col;
        return col;
    }

    public Column addPrimaryKeyColumn(String col, String type) {
        Column pk = new Column(this, col, type);
        return addPrimaryKeyColumn(pk);
    }

    public Column getColumn(String name) {
        if (!columns.containsKey(name)) {
            throw new RuntimeException(name + " is not a column in " + this);
        }
        return columns.get(name);
    }


    public Column addColumn(String col, String type) {
        return addColumn(new Column(this, col, type));
    }

    public Column addColumn(Column col) {
        Assertions.assertNotNull(col, "Can not add null column to " + this);
        Assertions.assertTrue(col.table() == this, "Can not add column of another table "
                + col.table() + " to " + this);
        Assertions.assertTrue(!contains(col), "Already contain column " + col + " in " + this);

        columns.put(col.name(), col);
        return col;
    }

    public Column getPrimaryKeyColumn() {
        Assertions.assertNotNull(pkColumn, "Primary key column is not defined for " + this);
        return pkColumn;
    }

    public boolean contains(Column col) {
        return columns.containsKey(col.name());
    }

    public Collection<Column> getColumns() {
        return columns.values();
    }

    public Collection<String> getColumnNames() {
        return columns.keySet();
    }

    public String name() {
        return name;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object other) {
        return Table.class.isInstance(other)
                && Table.class.cast(other).name().equalsIgnoreCase(name);
    }

    /**
     * Gets the foreign key to given taregt table
     * @param target
     * @return
     */
    public ForeignKey getForeignKey(Table target) {
        for (Map.Entry<String,Column> e : columns.entrySet()) {
            Column col = e.getValue();
            if (ForeignKey.class.isInstance(col)
                    && ForeignKey.class.cast(col).targetColumn().table().equals(target)) {
                return ForeignKey.class.cast(col);
            }
        }
        return null;
    }


}
