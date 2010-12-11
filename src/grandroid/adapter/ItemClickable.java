/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package grandroid.adapter;

import android.view.View;

/**
 *
 * @author Rovers
 */
public interface ItemClickable<T> {
    public void onClickItem(int index, View view,T item);
}
