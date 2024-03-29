/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package raingo.client;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * HTTP連線物件，用來擷取網頁的回傳結果，可用在取得JSON字串或網頁HTML的時候，支援POST。
 *
 * @author Rovers
 */
public class Mon {

    /**
     *
     */
    protected String uri;
    /**
     *
     */
    protected HashMap<String, String> param;
    protected String encoding;
    protected static String cookie = null;
    protected boolean keepingCookie = false;
    protected boolean loginConnection = false;

    ;

    /**
     *
     * @param uri 欲擷取資料的URL
     */
    public Mon(String uri) {
        this.uri = uri;
        this.encoding = "UTF-8";

        param = new HashMap<String, String>();
    }

    protected Mon getLoginMon() {
        return null;
    }

    protected Mon asLoginConnection() {
        loginConnection = true;
        return this;
    }

    protected void handleLogin(String result) {
    }

    public boolean isKeepingCookie() {
        return keepingCookie;
    }

    public void setKeepingCookie(boolean keepingCookie) {
        this.keepingCookie = keepingCookie;
    }

    /**
     * 伺服端服務網址
     *
     * @return
     */
    public String getUri() {
        return uri;
    }

    public Mon encode(String encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * 新增一組傳輸參數
     *
     * @param key 參數的名字
     * @param value 參數值
     * @return Mon物件本身，方便串接
     */
    public Mon put(String key, String value) {
        param.put(key, value);
        return this;
    }

    /**
     * 清除變數
     */
    public void clear() {
        param.clear();
    }

    /**
     * 取得參數內容 (實作方式為TreeMap.toString())
     *
     * @return
     */
    public String getParamaters() {
        TreeMap<String, String> tm = new TreeMap<String, String>();
        for (String key : param.keySet()) {
            tm.put(key, param.get(key));
        }
        return tm.toString();
    }

    /**
     * 開始連線傳輸，並將結果JSON文字包成JSONObject回傳
     *
     * @return server端回傳的JSON文字所包裝成的JSONObject
     * @throws JSONException
     */
    public JSONObject sendAndWrap() throws JSONException {
        return new JSONObject(send());
    }

    /**
     * 開始連線傳輸，並將結果JSON文字包成JSONArray回傳
     *
     * @return server端回傳的JSON文字所包裝成的JSONArray
     * @throws JSONException
     */
    public JSONArray sendAndWrapArray() throws JSONException {
        return new JSONArray(send());
    }

    /**
     * 開始連線傳輸
     *
     * @return server端回應的字串傳回
     */
    public String send() {
        try {
            URL url = new URL(uri);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            if (keepingCookie) {
                if (cookie == null) {
                    Log.d("grandroid", "login...");
                    Mon logMon = getLoginMon();
                    if (logMon != null) {
                        handleLogin(logMon.asLoginConnection().send());
                    } else {
                        throw new Exception("You must overwrite getLoginMon method in Mon first");
                    }
                }
            }
            if (!keepingCookie || cookie != null) {
                ////设置连接属性
                httpConn.setDoOutput(true);//使用 URL 连接进行输出
                httpConn.setDoInput(true);//使用 URL 连接进行输入
                httpConn.setUseCaches(false);//忽略缓存
                httpConn.setRequestMethod("POST");//设置URL请求方法
                String requestString = "";
                for (String key : param.keySet()) {
                    String encodedValue = key + "=" + URLEncoder.encode(param.get(key).replaceAll("\\\\/", "/"), "UTF-8");
                    requestString += requestString.length() == 0 ? encodedValue : "&" + encodedValue;
                }
                byte[] requestStringBytes = requestString.getBytes("UTF-8");
                //httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
                httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                httpConn.setRequestProperty("Charset", "UTF-8");
                if (keepingCookie && cookie != null) {
                    httpConn.setRequestProperty("Cookie", cookie);
                }
                httpConn.connect();
                //
                //建立输出流，并写入数据
                OutputStream outputStream = httpConn.getOutputStream();
                outputStream.write(requestStringBytes);
                outputStream.close();
                //获得响应状态
                int responseCode = httpConn.getResponseCode();
                if (HttpURLConnection.HTTP_OK == responseCode) {//连接成功
                    if (loginConnection) {
                        getCookie(httpConn);
                    }

                    //当正确响应时处理数据
                    StringBuilder sb = new StringBuilder();
                    String readLine;
                    BufferedReader responseReader;
                    if (httpConn.getHeaderField("Content-Type") != null && httpConn.getHeaderField("Content-Type").contains("charset")) {
                        encoding = httpConn.getHeaderField("Content-Type").substring(httpConn.getHeaderField("Content-Type").indexOf("charset=") + 8);
                        if (encoding.contains(";")) {
                            encoding = encoding.substring(0, encoding.indexOf(";"));
                        }
                    }
                    //处理响应流，必须与服务器响应流输出的编码一致
                    responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), encoding));
                    while ((readLine = responseReader.readLine()) != null) {
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                    return sb.toString().trim();
                } else {
                    return "{\"msg\":\"Mon connect fail\",\"code\":" + responseCode + "}";
                }
            } else {
                throw new Exception("Login fail in Mon");
            }
        } catch (Exception ex) {
            Log.e("grandroid", null, ex);
            return "{msg:\"" + ex.toString() + "\"}";
        }
    }

    protected void getCookie(HttpURLConnection connection) {
        String cookieVal = null;
        cookie = "";
        String key = null;
        for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("set-cookie")) {
                cookieVal = connection.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                cookie = cookie + cookieVal + ";";
            }
        }
        if (cookie.length() == 0) {
            cookie = null;
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        //Mon mon = new Mon("http://www.dingschool.com/raingo");
        //System.out.println(mon.put("col", "test").put("act", "+").put("json", "{\"target\":[\"ruby\",\"楊峻武\"],\"count\":2}").send());
        //System.out.println(mon.put("col", "test").put("act", "/target").put("json", "{\"target\":\"楊峻武\"}").send());
        //System.out.println(mon.put("col", "test").put("act", "&target,abc").put("json", "{\"target\":\"ruby\",\"x\":1}").send());
        //System.out.println(mon.put("col", "test").put("act", "*").send());
        //System.out.println(mon.put("col", "test").put("act", "?").put("json", "{\"target\":\"ruby\"}").send());
        //System.out.println(mon.put("col", "WordBank").put("act", "-").put("json","{ \"_id\" : { \"$oid\" : \"4c4423503cb9a6998cf85edb\"}}").send());
        Mon mon = new Mon("http://m.family.com.tw/api/active.php");
        try {
            //System.out.println(mon.put("page", "get_mission_now").put("user_id", "9").send());
            //System.out.println(mon.put("page", "get_store").put("user_id", "9").put("update_time_last", "null").send());
            //System.out.println(mon.put("page", "save_pvc").put("user_id", "56").put("pvc_id", "1").put("mission_id", "1").put("store_id", "005891").send());
            //System.out.println(mon.put("page", "get_pvc").put("user_id", "56").put("mission_id", "1").put("store_id", "005891").send());
            //System.out.println(mon.put("page", "get_mission_pvc").put("user_id", "9").put("mission_id", "1").send());
            //System.out.println(mon.put("page", "get_all_action").put("user_id", "62").send());
            //System.out.println(mon.put("page", "get_info").put("user_id", "9").send());
            //System.out.println(mon.put("page", "close").put("user_id", "62").send());
            //Mon mon = new Mon("http://family.hiiir.com/api/active.php");
            //System.out.println(mon.put("page", "get_store").put("user_id", "9").put("update_time_last", "2011-03-20 08:00:00").send());
            //System.out.println(mon.put("page", "restart").put("user_id", "62").put("user_latitude", "24.5").put("user_longitude", "121.5421").send());
            //System.out.println(mon.put("page", "get_lbs").put("user_id", "1").put("time", "2011-04-01 10:10:10").send());
            //System.out.println(mon.put("page", "get_lbs_message").put("user_id", "1").send());
            //System.out.println(mon.put("page", "get_food").put("group", "1").send());
            JSONArray arr = mon.put("page", "get_food").put("group", "1").sendAndWrap().getJSONArray("food");
            for (int i = 0; i < arr.length(); i++) {
                System.out.println(arr.get(i).toString());
            }
            //System.out.println(mon.put("page", "get_food_today").put("date", "2011-04-13").put("user_id", "1").send());
            //System.out.println(mon.put("page", "save_activity").put("date", "2011-04-13").put("user_id", "1").put("step", "210").put("expend_time", "312").put("calorie", "36").send());
            //System.out.println(mon.put("page", "get_calorie").put("date", "2011-04-13").put("user_id", "1").send());
            //System.out.println(mon.put("page", "get_calorie").put("date", "2011-04-13").put("user_id", "1").send());
        } catch (JSONException ex) {
            Logger.getLogger(Mon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
