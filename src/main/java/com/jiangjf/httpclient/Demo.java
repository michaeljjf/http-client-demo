package com.jiangjf.httpclient;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiangjf.bean.User;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangjf
 * @date 2021/10/1
 */
public class Demo {
    public static void main(String[] args) throws URISyntaxException, IOException {
//        testGet();
//        testPost();
//        testUserPost();
//        testUserListPost();
        testInputStream();
    }

    public static void testGet() throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder("http://localhost:8080/demo");
        uriBuilder.addParameter("param", "张三");
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        response.close();
        httpClient.close();
    }

    public static void testPost() throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/demo");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("param", "张三丰"));
        HttpEntity httpEntity = new UrlEncodedFormEntity(params, "utf-8");
        httpPost.setEntity(httpEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        response.close();
        httpClient.close();
    }

    public static void testUserPost() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/demo2");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("id", "111"));
        params.add(new BasicNameValuePair("name", "张三丰"));
        HttpEntity httpEntity = new UrlEncodedFormEntity(params, "utf-8");
        httpPost.setEntity(httpEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(result, User.class);
        System.out.println(user);

        String userJson = objectMapper.writeValueAsString(user);
        System.out.println(userJson);
    }

    public static void testUserListPost() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/demo3");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, User.class);
        List<User> users = objectMapper.readValue(result, javaType);
        System.out.println(users);
    }


    public static void testInputStream() throws IOException {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httpPost对象
        HttpPost httpPost = new HttpPost("http://localhost:8080/demo4");
        // 组织参数
        List<User> users = new ArrayList<>();
        users.add(new User(1, "张三丰"));
        users.add(new User(2, "张无忌"));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonParam = objectMapper.writeValueAsString(users);
        HttpEntity httpEntity = new StringEntity(jsonParam, ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        // 执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 处理响应
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        // 关闭资源
        response.close();
        httpClient.close();
    }
}

























