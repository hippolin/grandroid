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
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @param <T> 
 * @author Rovers
 */
public abstract class DataHelper<T extends Identifiable> {

    /**
     * 
     */
    protected FaceData fd;
    /**
     * 
     */
    protected String tableName;

    /**
     * 
     * @param fd
     * @param c
     */
    public DataHelper(FaceData fd, Class<T> c) {
        this(fd, c, true);
    }

    /**
     * 
     * @param fd
     * @param c
     * @param createTable
     */
    public DataHelper(FaceData fd, Class<T> c, boolean createTable) {
        this.fd = fd;
        Table ann = c.getAnnotation(Table.class);
        if (ann != null) {
            tableName = ann.value();
        } else {
            tableName = c.getSimpleName();
        }
        if (createTable) {
            create();
        }
    }

    /**
     * 
     * @return
     */
    public FaceData getFaceData() {
        return fd;
    }

    /**
     * 
     * @param fd
     */
    public void setFaceData(FaceData fd) {
        this.fd = fd;
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * 
     * @return
     */
    public boolean create() {
        fd.createTable(tableName, getCreationString());
        return true;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public boolean insert(T obj) {
        long result = -1;
        ContentValues cv = this.getKeyValues(obj);
        try {
            result = fd.insert(tableName, cv);
        } catch (Exception ex) {
            if (ex.toString().contains("has no column named")) {
                if (fixTable(cv)) {
                    try {
                        result = fd.insert(tableName, cv);
                    } catch (Exception ex1) {
                        Log.e("grandroid", null, ex1);
                    }
                }
            }
        }
        if (result > -1) {
            obj.set_id((int) result);
        }
        return result > -1;
    }

    /**
     * 
     * @param col
     * @return
     */
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

    /**
     * 
     * @param obj
     * @return
     */
    public boolean update(T obj) {
        return fd.update(tableName, this.getID(obj), this.getKeyValues(obj));
    }

    /**
     * 
     * @param col
     * @return
     */
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

    /**
     * 
     * @param id
     * @return
     */
    public boolean delete(Integer id) {
        return fd.delete(tableName, id);
    }

    /**
     * 
     * @param where
     * @return
     */
    public boolean delete(String where) {
        return fd.delete(tableName, where);
    }

    /**
     * 
     * @return
     */
    public boolean truncate() {
        return fd.truncate(tableName);
    }

    /**
     * 
     * @return
     */
    public boolean drop() {
        return fd.drop(tableName);
    }

    /**
     * 
     * @return
     */
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

    /**
     * 
     * @param where
     * @return
     */
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

    /**
     * 
     * @param where
     * @param start
     * @param count
     * @return
     */
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

    /**
     * 
     * @param id
     * @return
     */
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

    /**
     * 
     * @param where
     * @return
     */
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

    /**
     * 
     */
    public void close() {
        fd.close();
    }

    public boolean fixTable(ContentValues cv) {
        try {
            HashSet<String> set = new HashSet<String>();
            for (Entry<String, Object> entry : cv.valueSet()) {
                set.add(entry.getKey());
            }
            Cursor cursor = fd.selectSingle(tableName, 0);
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                set.remove(cursor.getColumnName(i));
            }
            cursor.close();
            fd.close();
            boolean result = true;
            if (set.size() > 0) {
                for (String key : set) {
                    result = result && fd.addColumn(tableName, key, getColumnType(key));
                }
                if (result) {
                    Log.d("grandroid", "fix table success");
                } else {
                    Log.e("grandroid", "fix table fail");
                }
            }
            return result;
        } catch (Exception ex) {
            Log.e("grandroid", "fix table fail", ex);
            return false;
        }
    }

    public TypeMapping getColumnType(String column) {
        return TypeMapping.STRING;
    }

    /**
     * 
     * @return
     */
    public abstract String getCreationString();

    /**
     * 
     * @param obj
     * @return
     */
    public abstract Integer getID(T obj);

    /**
     * 
     * @param obj
     * @return
     */
    public abstract ContentValues getKeyValues(T obj);

    /**
     * 
     * @param cursor
     * @return
     */
    public abstract T getObject(Cursor cursor);
}
