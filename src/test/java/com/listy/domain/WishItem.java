package com.listy.domain;

/**
 * Created by ppoddar on 7/12/16.
 */
public class WishItem extends DomainObject {
    private String name;
    private String quantity;

    @Override
    public Object getValue(String col) {
        return col;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String q) {
        this.quantity = q;
    }

}
