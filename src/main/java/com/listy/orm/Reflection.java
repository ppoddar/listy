package com.listy.orm;

import java.lang.reflect.Method;

/**
 * Created by ppoddar on 7/14/16.
 */
public class Reflection {
    public static final Class<?>[] EMPTY_ARGTYPES = {};
    public static final Object[] EMPTY_ARGS = {};

    public static boolean hasProperty(Class cls, String property) {
        return getGetter(cls, property) != null;
    }
    public static Method getGetter(Class<?> cls, String property, boolean mustExist) {
        Method getter = getGetter(cls, property);
        if (getter == null && mustExist) {
            throw new RuntimeException("No getter method for property [" + property + "] in " + cls);
        }
        return getter;
    }

    public static Method getGetter(Class<?> cls, String property) {
        String name = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
        return getMethod(cls, name, EMPTY_ARGTYPES);
    }

    public static Method getSetter(Class<?> cls, String property, boolean mustExist) {
        Method setter = getSetter(cls, property);
        if (setter == null && mustExist) {
            throw new RuntimeException("No setter method for property " + property + " in " + cls);
        }
        return setter;
    }

    public static Method getSetter(Class<?> cls, String property) {
        Method getter = getGetter(cls, property, false);
        if (getter == null) return null;
        String name = "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
        return getMethod(cls, name, new Class[]{getter.getReturnType()});
    }


    /**
     * Gets public method of given name and argument type.
     *
     * @param cls the class of the method
     * @param name name of a method
     * @param argTypes argument types
     * @return null if no such method exists
     */
    public static Method getMethod(Class<?> cls, String name, Class<?>[] argTypes) {
        try {
            return cls.getMethod(name, argTypes);
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (Exception ex) {
            throw new RuntimeException("error while looking for method " + name + " in " + cls, ex);
        }
    }


}

