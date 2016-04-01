package com.rain.monitor.trade.okCoin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rain.monitor.trade.okCoin.entity.Order;
import com.rain.monitor.trade.okCoin.stock.IStockRestApi;
import com.rain.monitor.trade.okCoin.stock.impl.StockRestApi;
import org.apache.http.HttpException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chendengyu on 2016/3/30.
 */
public class OkCoinLTCTrade {

    public static boolean REINIT = true;
    private static final String COIN = "ltc";
    private static final String TRADE = "cny";
    private static final String TRADECOIN = COIN+"_"+TRADE;


    private static String api_key = "";  //OKCoin�����apiKey
    private static String secret_key = "";  //OKCoin �����secret_key
    private static String url_prex = "https://www.okcoin.cn";  //ע�⣺����URL ����վhttps://www.okcoin.com ; ����վhttps://www.okcoin.cn
    /**
     * ��ȡ ��ǰ �û�����Ϣ
     * map.put("cny",free.getString("cny"));
     * map.put("ltc",free.getString("ltc"));
     * map.put("cny_freezed",free.getString("cny"));
     * map.put("ltc_freezed",free.getString("ltc"));
     */
    public static Map<String,String> getUserInfo( boolean force) throws IOException, HttpException {
        String userinfo = getPost().userinfo();
        println(userinfo);
        Map<String,String> map = null;
        JSONObject jsonObject = JSON.parseObject(userinfo);
        if("true".equals(jsonObject.getString("result")) || force)
        {
            map = new HashMap<>();
            JSONObject json = jsonObject.getJSONObject("info").getJSONObject("funds");
            JSONObject free = json.getJSONObject("free");
            JSONObject freezed = json.getJSONObject("freezed");
            map.put("cny",free.getString("cny"));
            map.put("coin",free.getString(COIN));
            map.put("cny_freezed",freezed.getString("cny"));
            map.put("coin_freezed",freezed.getString(COIN));
            REINIT = false;
        }
        return map;
    }

    /**
     * ���� Ŀǰ�۸��� ��
     */
    public static void allBuyNow(double price) throws IOException, HttpException {
        String userinfo = getPost().userinfo();
        println(userinfo);
        JSONObject jsonObject = JSON.parseObject(userinfo);
        if("true".equals(jsonObject.getString("result")))
        {
            JSONObject json = jsonObject.getJSONObject("info").getJSONObject("funds");
            JSONObject free = json.getJSONObject("free");
            JSONObject freezed = json.getJSONObject("freezed");
            double count = new Double(Double.parseDouble(free.getString("cny"))/price*10).intValue()/10D;
            if(count > 0.1)
            {
                REINIT = true;
                String result = getPost().trade(TRADECOIN,"buy",price+"",count+"");
                println(result);
            }
            double freezedLTC = Double.parseDouble(freezed.getString(COIN));
            //��������ȫȡ��
            if(freezedLTC>0.1)
            {
                List<Order> orders = getOrders();
                for(Order order:orders)
                {
                    if(!"buy".equalsIgnoreCase(order.getType()))
                    {
                        REINIT = true;
                        getPost().cancel_order(TRADECOIN,order.getOrder_id());
                    }
                }
            }
        }
    }

    /**
     * ���� Ŀǰ�۸� ����
     */
    public static void allSellNow(String price) throws IOException, HttpException {
        String userinfo = getPost().userinfo();
        println(userinfo);
        JSONObject jsonObject = JSON.parseObject(userinfo);
        if("true".equals(jsonObject.getString("result")))
        {
            JSONObject json = jsonObject.getJSONObject("info").getJSONObject("funds");
            JSONObject free = json.getJSONObject("free");
            JSONObject freezed = json.getJSONObject("freezed");
            String amount = free.getString(COIN);
            Double amountNum = Double.parseDouble(amount);
            amountNum = new Double(amountNum*1000).intValue()/1000D;

            double freezedLTC = Double.parseDouble(freezed.getString(COIN));
            double freezedCNY = Double.parseDouble(freezed.getString(TRADE));
            //��������ȫȡ��
            if(freezedLTC>0.1 || freezedCNY > 0.1)
            {
                List<Order> orders = getOrders();
                for(Order order:orders)
                {
                    REINIT = true;
                    getPost().cancel_order(TRADECOIN,order.getOrder_id());
                }
            }
            if(amountNum > 0.1)
            {
                REINIT = true;
                String result = getPost().trade(TRADECOIN, "sell", price, amountNum + "");
                println(result);
            }

        }
    }
    /**
     * ��ȡ ������Ϣ
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static  List<Order> getOrders() throws IOException, HttpException {
        //������ȡ�û�����
        String str = getPost().order_history(TRADECOIN, "0", "1", "20");
        JSONObject jsonObject = JSON.parseObject(str);
        JSONArray orders = jsonObject.getJSONArray("orders");

        List<Order> list = new ArrayList<>();
        if(orders != null && orders.size() > 0)
        {
            for(int i=0;i< orders.size();i++)
            {
                list.add((Order) orders.getObject(i, Order.class));
            }
        }
        return list;
    }

    private static IStockRestApi getPost()
    {
        if(stockPost != null )
        {
            return stockPost;
        }
        /**
         * post�����跢�������֤����ȡ�û����������Ϣʱ����Ҫָ��api_key,��secret_key�����������ǩ����
         * �˴��Թ��췽������api_key��secret_key,�������û���ط���ʱ�������ٴ��룬
         * ����post����֮ǰ����������Զ����ܣ�����ǩ����
         *
         */
        IStockRestApi stockPost = new StockRestApi(url_prex, api_key, secret_key);
        return stockPost;
    }
    private static IStockRestApi stockPost = null;
    public static void println(String data)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(new Date())+": "+data);
    }

}
