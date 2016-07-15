package com.listy.orm;

import android.database.Cursor;
import android.util.Log;

import com.listy.schema.Column;
import com.listy.schema.ForeignKey;
import com.listy.schema.Table;
import com.listy.util.Assertions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ppoddar on 7/14/16.
 */
public class BasicTypeMapping implements TypeMapping {
    private Class<?> cls;
    private Table table;

    private Map<String, FieldMapping> mappings = new LinkedHashMap<String,FieldMapping>();

    public BasicTypeMapping(Class<?> c, Table t) {
        Assertions.assertNotNull(c, "Can not map from null class");
        Assertions.assertNotNull(t, "Can not map to null table");
        cls = c;
        table = t;
    }

    @Override
    public Collection<FieldMapping> getMappings() {
        return mappings.values();
    }

    @Override
    public FieldMapping getMapping(String property) {
        return mappings.get(property);
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public Class<?> getObjectType() {
        return cls;
    }


    void defineMapping(String property, FieldMapping mapping) {
        //Reflection.getGetter(getObjectType(), property, true);
        mappings.put(property, mapping);
    }

    @Override
    public void populate(Object target, Cursor cursor) {
        for (FieldMapping mapping : mappings.values()) {
            mapping.populate(target, cursor);
        }
    }

    @Override
    public List<SQL> insertSQL(Object target, ORMContext ctx) {

        List<SQL> sqls = new ArrayList<SQL>();

        SQL sql = new SQL();
        for (FieldMapping mapping : mappings.values()) {
                mapping.insert(sql, target, target, ctx);
        }
        SQL basicSQL = new SQL().append("INSERT INTO")
                .append(table.name())
                .append(StringUtils.enclose('(', ')', sql.columns()))
                .append("VALUES")
                .append(StringUtils.enclose('(', ')', sql.binders()));

        basicSQL.copyBindVariables(sql);

        sqls.add(basicSQL);

        Set<Table> childTables = getChildTables();

        for (Table childTable : childTables) {
            FieldMapping mapping = getMappingToTable(childTable);
            Object value = mapping.extractValueFrom(target, ctx);
            Assertions.assertTrue(Collection.class.isInstance(value),
                    mapping.getProperty() + " is not a collection");
            Assertions.assertTrue(ContainerMapping.class.isInstance(mapping),
                    "Expected mapping to be an instance of " + ContainerMapping.class
                    + " but was " + mapping.getClass());

            TypeMapping elementTypeMapping = ContainerMapping.class.cast(mapping).getElementTypeMapping();
            Collection coll = Collection.class.cast(value);
            for (Object element : coll) {
                    List<SQL> childSQL = elementTypeMapping.insertSQL(element, ctx);
                    sqls.addAll(childSQL);
                }
        }

        return sqls;
    }

    @Override
    public Set<Table> getChildTables() {
        Set<Table> childTables = new HashSet<Table>();
        for (FieldMapping mapping : this) {
            if (mapping.getOwner() != this) {
                childTables.add(mapping.getOwner().getTable());
            }
        }
        return childTables;
    }

    @Override
    public FieldMapping getMappingToTable(Table t) {
        for (FieldMapping mapping : this) {
            if (mapping.getOwner() != this && mapping.getOwner().getTable() == t) {
                return mapping;
            }
        }
        throw new RuntimeException("no mapping maps to " + t);
    }

    @Override
    public SQL dropSchemaSQL() {
        return new SQL().append("DROP TABLE IF EXISTS").append(table.name());
    }

    /**
     * Returns an {@link Iterator} for the elements in this object.
     *
     * @return An {@code Iterator} instance.
     */
    @Override
    public Iterator<FieldMapping> iterator() {
        return mappings.values().iterator();
    }

    public SQL defineSchemaSQL() {
        SQL sql = new SQL().append("CREATE TABLE").append(table.name()).append("(");

        Iterator<FieldMapping> i = iterator();
        while (i.hasNext()) {
            StringBuilder fragment = i.next().defineSchema();
            fragment.append(i.hasNext() ? "," : "");
            sql.append(fragment);
        }

        Iterator<FieldMapping> j = iterator();
        StringBuilder constraints = new StringBuilder();
        while (j.hasNext()) {
            StringBuilder fragment = j.next().defineConstraint();
            if (fragment != null) {
                if (constraints.length() > 0) constraints.append(',');
                constraints.append(fragment);
            }
        }
        if (constraints.length() > 0) {
            sql.append(",");
            sql.append(constraints);
        }
        sql.append(")");

        return sql;
    }

    public void addBasicMapping(String property, Column column) {
        Assertions.assertTrue(Reflection.hasProperty(getObjectType(), property),
            "The type " + getObjectType().getName() + " does not have field " + property);
        Assertions.assertTrue(getTable().contains(column), "The target table " + getTable()
                + " must contain the mapped column " + column + " for mapping property " + property);

        FieldMapping mapping = new BasicFieldMapping(this, getObjectType(), property, column);
        defineMapping(property, mapping);
    }

    public void addPrimaryKeyMapping(String pkProperty) {
        PrimaryKeyFieldMapping mapping = new PrimaryKeyFieldMapping(this, pkProperty, table);
        defineMapping(pkProperty, mapping);
    }

    /**
     *
     * @param owner
     * @param cls
     * @param property name of a property designating a collection value
     * @param fk the database foreign key that represent the relation
     */
    public void addCollectionMapping(String property, ForeignKey fk, TypeMapping elementTypeMapping) {
        ContainerMapping mapping = new ContainerMapping(elementTypeMapping, getObjectType(), property, fk);

        defineMapping(property, mapping);

        elementTypeMapping.addElementMapping(property + "$element", fk);

    }

    public void addElementMapping(String property, ForeignKey fk) {
        ElementMapping mapping = new ElementMapping(this, fk);
        defineMapping(property, mapping);
    }


}

/**
 SQL parentSQL = new SQL();
 for (FieldMapping mapping : mappings.values()) {
 if (BasicFieldMapping.class == mapping.getClass()) {
 mapping.insert(parentSQL, target, null, ctx);
 } else if (PrimaryKeyFieldMapping.class == mapping.getClass()) {
 mapping.insert(parentSQL, target, null, ctx);
 } else if (ContainerMapping.class == mapping.getClass()) {
 Collection coll = (Collection)mapping.extractValueFrom(target, ctx);
 for (Object element : coll) {
 childSqls.addAll(
 ((ContainerMapping)mapping).getElementTypeMapping().insertSQL(element, ctx));
 }

 } else if (ElementMapping.class == mapping.getClass()) {
 mapping.insert(parentSQL, target, target, ctx);
 }
 }
 */
