/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Rovers
 */
public abstract class DataHelper<T extends Identifiable> {

    protected FaceData fd;
    protected String tableName;

    public DataHelper(FaceData fd, Class<T> c) {
        this(fd, c, true);
    }

    public DataHelper(FaceData fd, Class<T> c, boolean createTable) {
        this.fd = fd;
        Table ann = c.getAnnotation(Table.class);
        tableName = ann.value();
        if (createTable) {
            create();
        }
    }

    public FaceData getFaceData() {
        return fd;
    }

    public void setFaceData(FaceData fd) {
        this.fd = fd;
    }

    public boolean create() {
        fd.createTable(tableName, getCreationString());
        return true;
    }

    public boolean insert(T obj) {
        long result = fd.insert(tableName, this.getKeyValues(obj));
        if (result > -1) {
            obj.set_id((int) result);
        }
        return result > -1;
    }

    public boolean insert(Collection<T> col) {
        try {
            for (T obj : col) {
                long result = fd.insert(tableName, this.getKeyValues(obj));
                if (result > -1) {
                    obj.set_id((int) result);
                }
            }
            return true;
        } catch (Exception e) {
            Log.e(DataHelper.class.getName(), e.toString());
            return false;
        }
    }

    public boolean update(T obj) {
        return fd.update(tableName, this.getID(obj), this.getKeyValues(obj));
    }

    public boolean update(Collection<T> col) {
        try {
            boolean result = true;
            for (T obj : col) {
                result = result && fd.update(tableName, this.getID(obj), this.getKeyValues(obj));
            }
            return result;
        } catch (Exception e) {
            Log.e(DataHelper.class.getName(), e.toString());
            return false;
        }
    }

    public boolean delete(Integer id) {
        return fd.delete(tableName, id);
    }

    public boolean delete(String where) {
        return fd.delete(tableName, where);
    }

    public boolean truncate() {
        return fd.truncate(tableName);
    }

    public boolean drop() {
        return fd.drop(tableName);
    }

    public List<T> select() {
        Cursor cursor = fd.select(tableName);
        ArrayList<T> list = new ArrayList<T>();
        while (cursor.moveToNext()) {
            T obj = this.getObject(cursor);
            if (obj != null) {
                list.add(obj);
            }
        }
        cursor.close();
        fd.close();
        return list;
    }

    public List<T> select(String where) {
        Cursor cursor = fd.select(tableName, where);
        ArrayList<T> list = new ArrayList<T>();
        while (cursor.moveToNext()) {
            T obj = this.getObject(cursor);
            if (obj != null) {
                list.add(obj);
            }
        }
        cursor.close();
        fd.close();
        return list;
    }

    public List<T> select(String where, int start, int count) {
        Cursor cursor = fd.select(tableName, where, start, count);
        ArrayList<T> list = new ArrayList<T>();
        while (cursor.moveToNext()) {
            T obj = this.getObject(cursor);
            if (obj != null) {
                list.add(obj);
            }
        }
        cursor.close();
        fd.close();
        return list;
    }

    public T selectSingle(Integer id) {
        Cursor cursor = fd.selectSingle(tableName, id);
        T obj = null;
        if (cursor.moveToNext()) {
            obj = this.getObject(cursor);
        }
        cursor.close();
        fd.close();
        return obj;
    }

    public T selectSingle(String where) {
        Cursor cursor = fd.selectSingle(tableName, where);
        T obj = null;
        if (cursor.moveToNext()) {
            obj = this.getObject(cursor);
        }
        cursor.close();
        fd.close();
        return obj;
    }

    public void close() {
        fd.close();
    }

    public abstract String getCreationString();

    public abstract Integer getID(T obj);

    public abstract ContentValues getKeyValues(T obj);

    public abstract T getObject(Cursor cursor);
}
