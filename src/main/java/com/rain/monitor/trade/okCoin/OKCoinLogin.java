package com.rain.monitor.trade.okCoin;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Random;

/**
 * Created by chendengyu on 2016/3/28.
 */
public class OKCoinLogin {

    public static boolean checkIsLogin( CloseableHttpClient httpClient) throws IOException {
        String homePage = "https://www.okcoin.cn/";
        String loginPage = "https://www.okcoin.cn/user/login/index.do?random="+new Random().nextInt(100);
        HttpGet httpGet = new HttpGet(homePage);
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        System.out.println(EntityUtils.toString(execute.getEntity()));

        HttpPost httpPost = new HttpPost(loginPage);
//        postResult.
        CloseableHttpResponse postResult = httpClient.execute(httpPost);


        return false;
    }


    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = OKCoinConnect.get();
        checkIsLogin(httpClient);
    }


}
