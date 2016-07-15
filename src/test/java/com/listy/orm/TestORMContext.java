package com.listy.orm;

import com.listy.domain.DomainObject;
import com.listy.schema.DefaultORMContext;

public class TestORMContext extends DefaultORMContext {
    public Object primaryKey(Object obj) {
        return DomainObject.class.cast(obj).getId();
    }


}
