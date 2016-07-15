package com.listy.orm;

import com.listy.schema.ForeignKey;

/**
 * Maps collection elements to a separate table
 * Created by ppoddar on 7/14/16.
 */
public class ContainerMapping extends BasicFieldMapping  {
    /**
     *
     * @param owner type mapping that has a mapping that maps real data to real table
     * @param cls
     * @param property
     * @param fk
     */
    public ContainerMapping(TypeMapping owner, Class<?> cls, String property, ForeignKey fk) {
        super(owner, cls, property, fk, false);
        initMapping(Reflection.getGetter(cls, property, true),
                Reflection.getSetter(cls, property, false),
                fk);

//        Assertions.assertTrue(fk.table() == table(),
//                "Invalid ForeignKey mapping. The foreign key references " + fk.targetColumn().table()
//                        + " but this mapping is for " + table());
//

    }

    public TypeMapping getElementTypeMapping() {
        return owner;
    }

    @Override
    public void insert(SQL buffer, Object target, Object container, ORMContext ctx) {
        // nothing to do
    }
}
