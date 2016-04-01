package com.rain.monitor.trade.okCoin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rain.monitor.trade.formula.MACD;
import com.rain.monitor.trade.formula.StochRSI;
import org.apache.http.HttpException;
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
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by chendengyu on 2016/3/16.
 */
public class AutoTrade {
    //创建 线程池
    protected static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    //是否拥有LTC
    private static boolean hasCoin = false;
    //是否有挂单LTC
    private static boolean hasFreeCoin = false;
    //是否有挂单人民币
    private static boolean hasFreeCny = false;

    private static int times = 0;
    public static void main(String[] args) throws IOException, HttpException {

        syncMoneyStatus();
        scheduledThreadPool.scheduleAtFixedRate(new Thread() {
            @Override
            public void run() {
                times++;
                if(times%10 == 0)
                {
                    OkCoinLTCTrade.REINIT = true;
                }
                try {
                    println("========== "+times+" ===========");
                    getTradeInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (HttpException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public static void syncMoneyStatus() throws IOException, HttpException {
        if(OkCoinLTCTrade.REINIT)
        {
            Map<String, String> userInfo = OkCoinLTCTrade.getUserInfo(false);
            hasFreeCny = Double.parseDouble(userInfo.get("cny_freezed")) >= 0.1;
            hasFreeCoin = Double.parseDouble(userInfo.get("coin_freezed")) >= 0.1;
            hasCoin     = Double.parseDouble(userInfo.get("coin")) >= 0.1;
        }
    }
    /**
     * 获取 okcoin 的 交易的
     * 详细 数据
     * @throws IOException
     * @return
     */
    public static void getTradeInfo() throws IOException, HttpException {
        int type = 2 ;
        // 1  表示 5分钟线  ，2表示 15分钟线
        String url = "https://www.okcoin.cn/api/klineData.do?marketFrom=3&type="+type+"&limit=1000&coinVol=0";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        String result = EntityUtils.toString(execute.getEntity());
        //JSONArray 里的对象 也是数组，[1457892900000,2718.77,2719.51,2718.01,2718.64,4280.376]

        //从第一项分别 代表 时间，开盘价，最高价，最低价，收盘价，成交量
        JSONArray json = JSON.parseArray(result);
        List<Double> list = new ArrayList<>();
        for(int i=0;i<json.size();i++)
        {
            list.add(Double.parseDouble(json.getJSONArray(i).getString(4)));
        }
        Double fontPrice = list.get(list.size()-1);
        println("当前价格: "+fontPrice);
        //判断 MACD情况
        int posWay3 = StochRSI.checkStochRSI(list);
        println("[ rsi]  "+posWay3+"");
        int i = MACD.checkMACD(list);
        println("[macd]  "+i);
        if(posWay3%10 == 3 && posWay3!= 33 && i%10 == 3)
        {
            syncMoneyStatus();
            if(!hasCoin && !hasFreeCny)
            {
                println("建议买入");
                OkCoinLTCTrade.allBuyNow(fontPrice);
            }
        }
        if(posWay3%10 != 3 && i%10 != 3)
        {
            if(hasCoin || hasFreeCoin || hasFreeCny)
            {
                println("建议卖出");
                OkCoinLTCTrade.allSellNow(fontPrice+"");
            }
        }

    }

    public static void println(String data)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(new Date())+": "+data);
    }


}
