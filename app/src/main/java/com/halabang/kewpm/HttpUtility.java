package com.halabang.kewpm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Joey on 8/6/2016.
 */
public class HttpUtility {
    // 服务器地址
    private static String postURL = "http://www.google.com";

    public static void useHttpUrlConnectionGetThread(final String url, final ContentValues cv) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Utility.isEmptyString(url)) {
                    postURL = url;
                }
                useHttpUrlConnectionPost(postURL, cv);
            }
        }).start();
    }

    private static HttpURLConnection getHttpURLConnection(String url){
        HttpURLConnection mHttpURLConnection=null;
        try {
            URL mUrl=new URL(url);
            mHttpURLConnection=(HttpURLConnection)mUrl.openConnection();
            //设置链接超时时间
            mHttpURLConnection.setConnectTimeout(15000);
            //设置读取超时时间
            mHttpURLConnection.setReadTimeout(15000);
            //设置请求参数
            mHttpURLConnection.setRequestMethod("POST");
            //添加Header
            mHttpURLConnection.setRequestProperty("Connection","Keep-Alive");
            //接收输入流
            mHttpURLConnection.setDoInput(true);
            //传递参数时需要开启
            mHttpURLConnection.setDoOutput(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mHttpURLConnection ;
    }
    private static void postParams(OutputStream output, ContentValues obj) throws IOException{
        StringBuilder mStringBuilder=new StringBuilder();
        for (String key:obj.keySet()){
            if(!TextUtils.isEmpty(mStringBuilder)){
                mStringBuilder.append("&");
            }
            mStringBuilder.append(URLEncoder.encode(key,"UTF-8"));
            mStringBuilder.append("=");
            mStringBuilder.append(URLEncoder.encode(obj.get(key).toString(),"UTF-8"));
        }
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));
        writer.write(mStringBuilder.toString());
        writer.flush();
        writer.close();
    }
    private static void useHttpUrlConnectionPost(String url, ContentValues obj) {
        InputStream mInputStream = null;
        HttpURLConnection mHttpURLConnection = getHttpURLConnection(url);
        try {
            postParams(mHttpURLConnection.getOutputStream(), obj);
            mHttpURLConnection.connect();
            mInputStream = mHttpURLConnection.getInputStream();
            int code = mHttpURLConnection.getResponseCode();
            String response = Utility.convertStreamToString(mInputStream);
            Log.i("wangshu", "请求状态码:" + code + "\n请求结果:\n" + response);
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
