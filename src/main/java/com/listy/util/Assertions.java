package com.listy.util;

public class Assertions {
    
    public static void assertTrue(boolean condition, String msg) {
        if (!condition) {
            throw new RuntimeException(msg);
        }
    }
    
    public static void assertFalse(boolean condition, String msg) {
        assertTrue(!condition, msg);
    }

    public static void assertNotNull(Object object, String msg) {
        assertTrue(object != null, msg);
        
    }

}
