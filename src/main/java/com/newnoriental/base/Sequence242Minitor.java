package com.newnoriental.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by chendengyu on 2016/3/16.
 */
public class Sequence242Minitor extends MinitorBase{


    static String HOST = "http://172.18.4.242:15555";
    @Override
    public void execute() {
        scheduledThreadPool.scheduleAtFixedRate(new Thread() {
            @Override
            public void run() {
                try {
                    println("切图服务监控[" + HOST + "]开始");
                    CloseableHttpClient httpClient = HttpClients.createDefault();

                    String url = HOST + "/#/login";
                    HttpPut httpPut = new HttpPut(url);
                    List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
                    valuePairs.add(new BasicNameValuePair("username", "admin"));
                    valuePairs.add(new BasicNameValuePair("password", "dsjw2014"));
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, "utf-8");
                    httpPut.setEntity(entity);
                    CloseableHttpResponse response = httpClient.execute(httpPut);

                    EntityUtils.toString(response.getEntity());

                    String url2 = HOST + "/api/whoami";
                    HttpGet httpget2 = new HttpGet(url2);
                    httpget2.setHeader("authorization", "Basic YWRtaW46ZHNqdzIwMTQ=");
                    CloseableHttpResponse response2 = httpClient.execute(httpget2);
                    println("11:" + EntityUtils.toString(response2.getEntity()));

                    String url3 = HOST + "/api/queues";
                    HttpGet httpget3 = new HttpGet(url3);
                    httpget3.setHeader("authorization", "Basic YWRtaW46ZHNqdzIwMTQ=");
                    CloseableHttpResponse response3 = httpClient.execute(httpget3);
                    String entity1 = EntityUtils.toString(response3.getEntity());
                    JSONArray json = JSONArray.parseArray(entity1);
                    for (int m = 0; m < json.size(); m++) {
                        JSONObject jsonObject = (JSONObject) json.get(m);
                        if ("convertToPngQueue".equalsIgnoreCase(jsonObject.getString("name"))) {
                            String messages = jsonObject.getString("messages");
                            if (messages != null && messages.length() > 0) {
                                if (Long.parseLong(messages) > 100) {
                                    println("convertToPngQueue size  " + messages);
                                    String sendTo = "chendengyu@okjiaoyu.cn,wangchenxi@okjiaoyu.cn";
                                    String title = "切题服务["+HOST+"]监控报错，队列数值["+messages+"]";
                                    sendToMain(sendTo,title,title);
                                }
                            }
                        }
                    }
                    response2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    println("==========");
                }
                println("切图服务监控[" + HOST + "]结束");
            }
        }, 0, 60, TimeUnit.MINUTES);
    }

}

