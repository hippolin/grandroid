/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package raingo.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Rovers
 */
public class Mon {

    protected String uri;
    protected HashMap<String, String> param;

    public Mon(String uri) {
        this.uri = uri;
        param = new HashMap<String, String>();
    }

    public Mon put(String key, String value) {
        param.put(key, value);
        return this;
    }

    public JSONObject sendAndWrap() throws JSONException {
        return new JSONObject(send());
    }

    public String send() {
        try {
            URL url = new URL(uri);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            ////设置连接属性
            httpConn.setDoOutput(true);//使用 URL 连接进行输出
            httpConn.setDoInput(true);//使用 URL 连接进行输入
            httpConn.setUseCaches(false);//忽略缓存
            httpConn.setRequestMethod("POST");//设置URL请求方法
            String requestString = "";
            for (String key : param.keySet()) {
                String encodedValue = key + "=" + URLEncoder.encode(param.get(key), "UTF-8");
                requestString += requestString.length() == 0 ? encodedValue : "&" + encodedValue;
            }
            byte[] requestStringBytes = requestString.getBytes("UTF-8");
            //httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");
            //

            //建立输出流，并写入数据
            OutputStream outputStream = httpConn.getOutputStream();
            outputStream.write(requestStringBytes);
            outputStream.close();
            //获得响应状态
            int responseCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {//连接成功

                //当正确响应时处理数据
                StringBuffer sb = new StringBuffer();
                String readLine;
                BufferedReader responseReader;
                //处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
                return sb.toString().trim();
            } else {
                return "{msg:\"NG\",code:" + responseCode + "}";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "{msg:\"" + ex.toString() + "\"}";
        }
    }

    public static void main(String[] args) {
        //Mon mon = new Mon("http://www.dingschool.com/raingo");
        //System.out.println(mon.put("col", "test").put("act", "+").put("json", "{\"target\":[\"ruby\",\"楊峻武\"],\"count\":2}").send());
        //System.out.println(mon.put("col", "test").put("act", "/target").put("json", "{\"target\":\"楊峻武\"}").send());
        //System.out.println(mon.put("col", "test").put("act", "&target,abc").put("json", "{\"target\":\"ruby\",\"x\":1}").send());
        //System.out.println(mon.put("col", "test").put("act", "*").send());
        //System.out.println(mon.put("col", "test").put("act", "?").put("json", "{\"target\":\"ruby\"}").send());
        //System.out.println(mon.put("col", "WordBank").put("act", "-").put("json","{ \"_id\" : { \"$oid\" : \"4c4423503cb9a6998cf85edb\"}}").send());
        Date dt = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, -1 * dt.getDay());
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, 7 - dt.getDay());
        System.out.println(c1.getTime() + " ~ " + c2.getTime());
    }
}
