package com.listy.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ppoddar on 7/12/16.
 */
public class WishList extends DomainObject {
    private String name;
    private String description;
    private List<WishItem> items;

    public WishList() {
        super();
        setName("no name");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WishItem> getItems() {
        return items;
    }

    public void addItems(List<WishItem> items) {
        if (items == null) return;
        for (WishItem item : items) addItem(item);
    }

    public void addItem(WishItem item) {
        if (items == null) {
            items = new ArrayList<WishItem>();
        }
        items.add(item);
    }


    @Override
    public Object getValue(String col) {
        if ("name".equalsIgnoreCase(col)) {
            return getName();
        } else if ("description".equalsIgnoreCase(col)) {
            return getDescription();
        } else {
            throw new RuntimeException("unknown property " + col);
        }
    }
}
