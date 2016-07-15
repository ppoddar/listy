package com.listy.orm;

/**
 * Created by ppoddar on 7/14/16.
 */
public class DefaultNamingPolicy implements NamingPolicy {

    public static NamingPolicy instance = new DefaultNamingPolicy();
    @Override
    public String classToTable(Class javaClass) {
        return javaClass.getSimpleName().toUpperCase();
    }

    @Override
    public String propertyToColumn(String property) {
        return property.toUpperCase();
    }
}
