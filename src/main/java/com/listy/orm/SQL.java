package com.listy.orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ppoddar on 7/14/16.
 */
public class SQL {
    Map<String, Object> bindVariableValues = new HashMap<String, Object>();
    List<String> columnNames = new ArrayList<String>();


    List<StringBuilder> buffers = new ArrayList<StringBuilder>();

    public void appendColumnValue(String columnName, Object value) {
        columnNames.add(columnName);
        bindVariableValues.put(columnName, value);
    }

    public SQL append(String s) {
        return append(new StringBuilder(s));
    }

    public SQL append(StringBuilder s) {
        buffers.add(s);
        return this;
    }

    public String toString() {
        return StringUtils.join(' ', buffers.iterator()).toString();
    }

    public StringBuilder columns() {
        return StringUtils.join(',', columnNames.iterator());
    }

    public StringBuilder binders() {
        return StringUtils.joinRepeat(',', "?", bindVariableValues.size());
    }

    public int getBindVariableCount() {
        return bindVariableValues.size();
    }

    public Object getBindVariableValue(String var) {
        return bindVariableValues.get(var);
    }

    public void copyBindVariables(SQL other) {
        bindVariableValues = other.bindVariableValues;
    }


}

