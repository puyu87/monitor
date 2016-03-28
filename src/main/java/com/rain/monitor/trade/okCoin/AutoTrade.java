package com.rain.monitor.trade.okCoin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rain.monitor.trade.formula.MACD;
import com.rain.monitor.trade.formula.StochRSI;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chendengyu on 2016/3/16.
 */
public class AutoTrade {

    public static void main(String[] args) throws IOException {
        getTradeInfo();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = simpleDateFormat.format(new Date(1458470100000L));
        String format2 = simpleDateFormat.format(new Date(1458470400000L));
        System.out.println(format1);
        System.out.println(format2);
    }

    /**
     * 获取 okcoin 的 交易的
     * 详细 数据
     * @throws IOException
     */
    public static void getTradeInfo() throws IOException {
        int type = 1 ;
        // 5分钟 线
        //String url = "https://www.okcoin.cn/api/klineData.do?marketFrom=0&type=1&limit=1000&coinVol=0";
        //日 线
        String url = "https://www.okcoin.cn/api/klineData.do?marketFrom=0&type="+type+"&limit=1000&coinVol=0";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String result = EntityUtils.toString(execute.getEntity());
        System.out.println(result);
        //JSONArray 里的对象 也是数组，[1457892900000,2718.77,2719.51,2718.01,2718.64,4280.376]



        //从第一项分别 代表 时间，开盘价，最高价，最低价，收盘价，成交量
        JSONArray json = JSON.parseArray(result);
        List<Double> list = new ArrayList<>();
        for(int i=0;i<json.size();i++)
        {
            list.add(Double.parseDouble(json.getJSONArray(i).getString(4)));
        }
        //判断 MACD情况
        int posWay3 = StochRSI.checkStochRSI(list);
        System.out.println(posWay3);

        int i = MACD.checkMACD(list);
        System.out.println(i);
        System.out.println(json);
    }


}
