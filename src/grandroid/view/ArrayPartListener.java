/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package grandroid.view;

import java.util.List;

/**
 *
 * @author Rovers
 */
public interface ArrayPartListener<T> {
    public  List<T> loadArray();

    public boolean beforeFaceChange(int indexIn, int indexOut);

    public void afterFaceChange(int indexIn, int indexOut);
}
