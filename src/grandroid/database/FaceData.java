/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.SimpleDateFormat;

/**
 *
 * @author Rovers
 */
public class FaceData extends SQLiteOpenHelper {

    /**
     * 
     */
    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    /**
     * 
     */
    protected SQLiteDatabase db4read;

    /**
     * 
     * @param context
     * @param dbName
     */
    public FaceData(Context context, String dbName) {
        super(context, dbName, null, 1);
        db4read = null;
    }

    /**
     * 
     * @param arg0
     */
    @Override
    public void onCreate(SQLiteDatabase arg0) {
    }

    /**
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     */
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }

//    protected void setContentValues(ContentValues cv, String keyValues) {
//        String[] keyValuePair = keyValues.split(",");
//        for (int i = 0; i < keyValuePair.length; i++) {
//            String[] keyValue = keyValuePair[i].split("=");
//            if (keyValue.length == 2) {
//                cv.put(keyValue[0].trim(), keyValue[1].trim());
//            }
//        }
//    }
    /**
     * 
     * @param tableName
     * @param fieldPart
     * @return
     */
    public boolean createTable(String tableName, String fieldPart) {
        SQLiteDatabase db4write = null;
        try {
            db4write = this.getWritableDatabase();
            db4write.execSQL("create TABLE IF NOT EXISTS " + tableName + " (_id INTEGER primary key autoincrement, " + fieldPart + ")");
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
            return false;
        } finally {
            db4write.close();
        }
    }

    /**
     * 
     * @param tableName
     * @param cv
     * @return
     */
    public long insert(String tableName, ContentValues cv) {
        SQLiteDatabase db4write = null;
        try {
            db4write = this.getWritableDatabase();
            return db4write.insert(tableName, null, cv);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
            return -1;
        } finally {
            db4write.close();
        }
    }

    /**
     * 
     * @param tableName
     * @param index
     * @param cv
     * @return
     */
    public boolean update(String tableName, int index, ContentValues cv) {
        SQLiteDatabase db4write = null;
        try {
            String where = "_id=?";
            db4write = this.getWritableDatabase();
            db4write.update(tableName, cv, where, new String[]{Integer.toString(index)});
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
            return false;
        } finally {
            db4write.close();
        }
    }

    /**
     * 新增欄位可用"ALTER TABLE Photo ADD COLUMN dishKey TEXT;"
     * @param sql
     * @return
     */
    public boolean exec(String sql) {
        SQLiteDatabase db4write = null;
        try {
            db4write = this.getWritableDatabase();
            db4write.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
            return false;
        } finally {
            db4write.close();
        }
    }

    /**
     * 
     * @param tableName
     * @param index
     * @return
     */
    public boolean delete(String tableName, int index) {
        SQLiteDatabase db4write = null;
        try {
            String where = "_id=?";
            db4write = this.getWritableDatabase();
            db4write.delete(tableName, where, new String[]{Integer.toString(index)});
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
            return false;
        } finally {
            db4write.close();
        }
    }

    /**
     * 
     * @param tableName
     * @param where
     * @return
     */
    public boolean delete(String tableName, String where) {
        SQLiteDatabase db4write = null;
        try {
            db4write = this.getWritableDatabase();
            db4write.execSQL("Delete from " + tableName + " " + where);
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
            return false;
        } finally {
            db4write.close();
        }
    }

    /**
     * 
     * @param tableName
     * @return
     */
    public boolean truncate(String tableName) {
        SQLiteDatabase db4write = null;
        try {
            db4write = this.getWritableDatabase();
            db4write.delete(tableName, null, null);
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
            return false;
        } finally {
            db4write.close();
        }
    }

    /**
     * 
     * @param tableName
     * @return
     */
    public boolean drop(String tableName) {
        SQLiteDatabase db4write = null;
        try {
            db4write = this.getWritableDatabase();
            db4write.execSQL("drop table if exists " + tableName);
            return true;
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.toString());
            return false;
        } finally {
            db4write.close();
        }
    }

    /**
     * 
     * @param tableName
     * @return
     */
    public Cursor select(String tableName) {
        prepareDatabase();
        Cursor cursor = db4read.query(tableName, null, null, null, null, null, null);
        return cursor;
    }

    /**
     * 
     * @param tableName
     * @param where
     * @return
     */
    public Cursor select(String tableName, String where) {
        prepareDatabase();
        Cursor cursor = db4read.rawQuery("Select * from " + tableName + " " + where, null);
        return cursor;
    }

    /**
     * 
     * @param tableName
     * @param where
     * @param start
     * @param count
     * @return
     */
    public Cursor select(String tableName, String where, int start, int count) {
        prepareDatabase();
        Cursor cursor = db4read.rawQuery("Select * from " + tableName + " " + where + " limit " + start + "," + count, null);
        return cursor;
    }

    /**
     * 
     * @param tableName
     * @param id
     * @return
     */
    public Cursor selectSingle(String tableName, int id) {
        prepareDatabase();
        String where = "_id=?";
        Cursor cursor = db4read.query(tableName, null, where, new String[]{Integer.toString(id)}, null, null, null);
        return cursor;
    }

    /**
     * 
     * @param tableName
     * @param where
     * @return
     */
    public Cursor selectSingle(String tableName, String where) {
        prepareDatabase();
        Cursor cursor = db4read.rawQuery("Select * from " + tableName + " " + where + "  LIMIT 1", null);
        return cursor;
    }

    /**
     * 
     * @param sql
     * @return
     */
    public Cursor query(String sql) {
        prepareDatabase();
        Cursor cursor = db4read.rawQuery(sql, null);
        return cursor;
    }

    /**
     * 
     */
    public void prepareDatabase() {
        if (db4read != null) {
            if (db4read.isOpen()) {
                return;
            } else {
                db4read.releaseReference();
                db4read = null;
            }
        }
        db4read = this.getReadableDatabase();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

//    public void close() {
//        if (db4read != null && db4read.isOpen()) {
//            db4read.close();
//        }
//    }
}
