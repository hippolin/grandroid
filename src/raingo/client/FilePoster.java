/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package raingo.client;

import java.io.File;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Rovers
 */
public class FilePoster {

    public static String post(String url, File file) throws Exception {
        return post(url, file, null, null);
    }

    public static String post(String url, File file, String col, String json) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        HttpPost httppost = new HttpPost(url);

        PostFileEntity mpEntity = new PostFileEntity();
        if (col != null && col.length() > 0) {
            mpEntity.addPart("col", col);
        }
        if (json != null && json.length() > 0) {
            mpEntity.addPart("json", json);
        }
        mpEntity.addPart("uploadfile", file);
        httppost.setEntity(mpEntity);
        //System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();

        //System.out.println(response.getStatusLine());
        String responseText = "";
        if (resEntity != null) {
            responseText = EntityUtils.toString(resEntity);
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }

        httpclient.getConnectionManager().shutdown();
        return responseText.trim();
    }
//
//    public static String post(String url, File file) throws Exception {
//        HttpClient httpclient = new DefaultHttpClient();
//        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//
//        HttpPost httppost = new HttpPost(url);
//
//        PostFileEntity mpEntity = new PostFileEntity();
//
//        mpEntity.addPart("uploadfile", file);
//        httppost.setEntity(mpEntity);
//        //System.out.println("executing request " + httppost.getRequestLine());
//        HttpResponse response = httpclient.execute(httppost);
//        HttpEntity resEntity = response.getEntity();
//
//        //System.out.println(response.getStatusLine());
//        String responseText = "";
//        if (resEntity != null) {
//            responseText = EntityUtils.toString(resEntity);
//        }
//        if (resEntity != null) {
//            resEntity.consumeContent();
//        }
//
//        httpclient.getConnectionManager().shutdown();
//        return responseText.trim();
//    }
}
