package com.listy.orm;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.listy.domain.WishItem;
import com.listy.domain.WishList;
import com.listy.schema.Column;
import com.listy.schema.DefaultORMContext;
import com.listy.schema.ForeignKey;
import com.listy.schema.Table;

import android.provider.BaseColumns;

/**
 * Created by ppoddar on 7/14/16.
 */
public class MappingTest {
    private static ORMContext orm;
    
    @BeforeClass
    public static void defined() {
        
        orm = new TestORMContext();

        NamingPolicy namingPolicy = DefaultNamingPolicy.instance;

        Table containerTable = new Table(namingPolicy.classToTable(WishList.class));
        Table elementsTable = new Table(namingPolicy.classToTable(WishItem.class));

        // Android SQLite
        Column containerPK = containerTable.addPrimaryKeyColumn(BaseColumns._ID, "NUM");
        Column elementsPK = elementsTable.addPrimaryKeyColumn(BaseColumns._ID, "NUM");


        ForeignKey fk = new ForeignKey(elementsTable, "LIST_ID", "NUM", containerPK);

        TypeMapping containerMapping = new BasicTypeMapping(WishList.class, containerTable);
        TypeMapping elementsMapping = new BasicTypeMapping(WishItem.class, elementsTable);


        containerMapping.addPrimaryKeyMapping("id");
        containerMapping.addBasicMapping("name", containerTable.addColumn("NAME", "TEXT"));
        containerMapping.addBasicMapping("description", containerTable.addColumn("DESCRIPTION", "TEXT"));




        elementsMapping.addPrimaryKeyMapping("id");
        elementsMapping.addBasicMapping("name", elementsTable.addColumn("NAME", "TEXT"));
        elementsMapping.addBasicMapping("quantity", elementsTable.addColumn("QUANTITY", "TEXT"));

        containerMapping.addCollectionMapping("items", fk, elementsMapping);

        
        orm.addMapping(WishList.class, containerMapping);
        orm.addMapping(WishItem.class, elementsMapping);

         
    }

    @Test
    public void testSchemaDefinitionForContainerTable() throws Exception {
        SQL sql = orm.createSchemaDefinitionSQL(WishList.class);
        String expected = "CREATE TABLE WISHLIST"
                 + " ( _id NUM, NAME TEXT, DESCRIPTION TEXT, LIST_ID NUM"
                 + " , PRIMARY KEY(_id) )";
        Assert.assertEquals(expected, sql.toString());
     }

    @Test
    public void testSchemaDefinitionForElementTable() throws Exception {
        SQL sql = orm.createSchemaDefinitionSQL(WishItem.class);
        String expected = "CREATE TABLE WISHITEM"
                + " ( _id NUM, NAME TEXT, QUANTITY TEXT, LIST_ID NUM"
                + " , PRIMARY KEY(_id)"
                + ",FOREIGN KEY (LIST_ID) REFERENCES WISHLIST(_id) )";
        Assert.assertEquals(expected, sql.toString());
    }

    @Test
    public void testInsertContainerSQL() throws Exception {
        WishList list = new WishList();
        list.setName("Test");
        list.setDescription("Description");
        int N = 10;
        for (int i = 0; i < N; i++) {
            WishItem item = new WishItem();
            item.setName("item-"+i);
            list.addItem(item);
        }
        List<SQL> sqls = orm.createInsertSQL(list);

        Assert.assertEquals(N+1, sqls.size());



        SQL sql = sqls.get(0);

        String expected = "INSERT INTO WISHLIST (_id,NAME,DESCRIPTION) VALUES (?,?,?)";
        Assert.assertEquals(expected, sql.toString());
        Assert.assertEquals(3, sql.getBindVariableCount());
        Assert.assertEquals(list.getName(), sql.getBindVariableValue("NAME"));
        Assert.assertEquals(list.getDescription(), sql.getBindVariableValue("DESCRIPTION"));

        for (int i = 1; i <= N; i++) {
            SQL childSQL = sqls.get(i);

            expected = "INSERT INTO WISHITEM (_id,NAME,QUANTITY,LIST_ID) VALUES (?,?,?,?)";
            Assert.assertEquals(expected, childSQL.toString());
        }



    }



/**
    public void testOneToOneFieldMapping() throws Exception {
        WishList list = new WishList();
        WishItem item = new WishItem();
        list.addItem(item);

        TypeMapping typeMapping = new BasicTypeMapping(WishList.class, new Table("WISHLIST"));
        List<SQL> sqls = typeMapping.insert(list, null);

        Assert.assertEquals(2, sqls.size());
        Assert.assertEquals("INSERT INTO WISHLIST (ID,NAME) VALUES (?,?)", sqls.get(0).toString());
        Assert.assertEquals("INSERT INTO WISHITEM (ID,NAME,LIST_ID) VALUES (?,?,?)", sqls.get(1).toString());
    }
*/

}
