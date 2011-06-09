/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.database;

import android.database.Cursor;

/**
 *
 * @author Rovers
 */
public abstract class CursorVisitor {

    public abstract boolean handleRow(Cursor cursor);
    
    public void afterVisit(){
        
    }

    public void onError(Exception ex) {
    }
}
