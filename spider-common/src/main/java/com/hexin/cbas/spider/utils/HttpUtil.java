package com.hexin.cbas.spider.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: huangjiarong
 * @create: 2021/9/15
 * @version: v1.0
 */
@Component
public class HttpUtil {

    private static CloseableHttpClient HTTP_CLIENT;

    static {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager() {
        };
        manager.setMaxTotal(50);
        HTTP_CLIENT = HttpClients.custom().setConnectionManager(manager).build();
    }

    public static String httpGet(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(get)) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    public static String httpGet(String url, Map<String, String> headers) throws IOException {
        HttpGet get = new HttpGet(url);
        headers.forEach(get::addHeader);
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(get)) {
            return EntityUtils.toString(response.getEntity());
        }
    }


    public static String httpPost(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        HttpPost post = new HttpPost(url);
        headers.forEach(post::addHeader);
        List<NameValuePair> parameters = new ArrayList<>();
        params.forEach((key, value) -> parameters.add(new BasicNameValuePair(key, value)));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
        post.setEntity(entity);
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        }
    }


    public static String httpPostJson(String url, String body, Map<String, String> headers) throws IOException {
        HttpPost post = new HttpPost(url);
        headers.forEach(post::addHeader);
        post.setEntity(new StringEntity(body));
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(post)) {
            return EntityUtils.toString(response.getEntity());
        }
    }


}
