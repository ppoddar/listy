package com.listy.orm;

/**
 * Created by ppoddar on 7/14/16.
 */
public interface NamingPolicy {
    String classToTable(Class javaClass);
    String propertyToColumn(String property);
}
