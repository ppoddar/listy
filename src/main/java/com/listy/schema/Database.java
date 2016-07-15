package com.listy.schema;

import java.util.Set;

import com.listy.orm.ORMContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A SQLite database for Android to insert/update/query/delete persistent Java objects.
 *
 * Created by ppoddar on 7/12/16.
 */
public class Database extends SQLiteOpenHelper {

    private Object schemaIsDefined = new Object();
    private ORMContext orm;

    public Database(Context ctx, String name) {

        super(ctx, name, null, 1);
        ctx.databaseList();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        defineSchema(db, null);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropSchema(db, null);
        defineSchema(db, null);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropSchema(db, null);
        defineSchema(db, null);
    }

    /**
     * Defines schema and blocks until schema is defined.
     * @param db
     */
    void defineSchema(SQLiteDatabase db, Set<Class<?>> model) {
        for (Class<?> cls : model) {
            db.execSQL(orm.createSchemaDefinitionSQL(cls).toString());
        }
        synchronized (schemaIsDefined) {
            schemaIsDefined.notifyAll();
        }
    }

    void dropSchema(SQLiteDatabase db, Set<Class<?>> model) {
        for (Class<?> cls : model) {
            db.execSQL(orm.createDropTableSQL(cls).toString());
        }
    }


    public void insert(Object m) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = orm.getContentValues(m);
        db.insert(orm.getTable(m.getClass()).name(), null, values);
    }



    public Cursor getWishList() {
        SQLiteDatabase db = this.getWriteableDatabase();
        Cursor cursor = db.query(false, "WishList", null, null, null, null, null, null, null, null);
        return cursor;
    }

    public SQLiteDatabase getWriteableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();

        synchronized (schemaIsDefined) {
            try {
                schemaIsDefined.wait(5*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return db;
    }

}
