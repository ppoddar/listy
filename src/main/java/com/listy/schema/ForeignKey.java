package com.listy.schema;

/**
 * Created by ppoddar on 7/13/16.
 */
public class ForeignKey extends Column {
    private Column target;

    /**
     *
     * @param owner table where this column resides
     * @param name name of this column
     * @param type data type of this column
     * @param target the column to which this column references/points
     */
    public ForeignKey(Table owner, String name, String type, Column target) {
        super(owner, name, type);
        this.target = target;
        setConstraint("FOREIGN KEY (" + name() + ") REFERENCES (" + target.fullName() + ")");
    }

    public Column targetColumn() {
        return target;
    }

}
