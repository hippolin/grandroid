/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import java.lang.reflect.Field;

/**
 *
 * @author Rovers
 */
public class GenericHelper<T extends Identifiable> extends DataHelper<T> {

    protected Class<T> classData;
    protected TypeMapping[] types;
    protected Field[] fields;
    protected int indexID;

    public GenericHelper(FaceData fd, Class<T> objClass) {
        this(fd, objClass, true);
    }

    public GenericHelper(FaceData fd, Class<T> objClass, boolean createTable) {
        super(fd, objClass, false);
        classData = objClass;
        Field[] farr = objClass.getDeclaredFields();
        types = new TypeMapping[farr.length];
        fields = new Field[farr.length];
        for (int idx = 0; idx < farr.length; idx++) {
            fields[idx] = farr[idx];
            fields[idx].setAccessible(true);
            types[idx] = TypeMapping.getType(farr[idx].getType());
        }
        if (createTable) {
            this.create();
        }
    }

    @Override
    public String getCreationString() {
        String str = "";
        for (int i = 0; i < fields.length; i++) {
            if (i != indexID) {
                str += str.length() == 0 ? fields[i].getName() + " " + types[i].getSqlType() : ", " + fields[i].getName() + " " + types[i].getSqlType();
            }
        }
        return str;
    }

    @Override
    public Integer getID(T obj) {
        return obj.get_id();
    }

    @Override
    public ContentValues getKeyValues(T obj) {

        ContentValues kvs = new ContentValues();
        for (int i = 0; i < fields.length; i++) {
            if (i != indexID) {
                try {
                    Object value = fields[i].get(obj);
                    if (value != null) {
                        //str += str.length() == 0 ? fields[i].getName() + "=" + types[i].envalue(value) : ", " + fields[i].getName() + "=" + types[i].envalue(value);
                        kvs.put(fields[i].getName(), types[i].envalue(value).toString());
                    } else {
                        Log.w(this.getClass().getName(), "getter " + fields[i].getName() + " has error (value is null)");
                    }

                } catch (Exception ex) {
                    Log.e(this.getClass().getName(), null, ex);
                }
            }
        }
        //Log.i(this.getClass().getName(), "obj : " + str);
        return kvs;
    }

    @Override
    public T getObject(Cursor cursor) {
        try {
            T obj = classData.newInstance();
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].set(obj, types[i].getResultSetValue(cursor, fields[i].getName()));
                } catch (Exception ex) {
                    Log.e(this.getClass().getName(), "%%%%%%%%%%%%%%" + fields[i] + "%%%%%%%%%%%%%");
                    Log.e(this.getClass().getName(), null, ex);
                }
            }
            return obj;
        } catch (Exception ex) {
            Log.e(this.getClass().getName(), null, ex);
            return null;
        }
    }

    public <S extends Identifiable> GenericHelper<S> createHelper(Class<S> c) {
        return new GenericHelper<S>(fd, c);
    }

    public <S extends Identifiable> GenericHelper<S> createHelper(Class<S> c, boolean createTable) {
        return new GenericHelper<S>(fd, c, createTable);
    }
}
