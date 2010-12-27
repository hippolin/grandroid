/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grandroid.view;

import java.util.List;

/**
 * 註冊給ArrayPart的callback物件
 * @param <T> 物件陣列的型別
 * @author Rovers
 */
public interface ArrayPartListener<T> {

    /**
     * 載入陣列
     * @return 欲傳給ArrayPart的物件陣列
     */
    public List<T> loadArray();

    /**
     * 在頁面改變之前被執行
     * @param indexIn 新的index
     * @param indexOut 舊的index
     * @return 回傳true則繼續執行，回傳false
     */
    public boolean beforeFaceChange(int indexIn, int indexOut);

    /**
     * 在頁面改變之後被執行
     * @param indexIn 新的index
     * @param indexOut 舊的index
     */
    public void afterFaceChange(int indexIn, int indexOut);
}
