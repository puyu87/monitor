package com.rain.monitor.btc38;

import java.util.ArrayList;
import java.util.List;
import static com.rain.monitor.btc38.util.Functions.*;
/**
 * Created by chendengyu on 2016/3/18.
 */
public class StochRSI {
    /**
     * 返回 是否 stochRsi
     * 11 代表 低位向下
     * 12 代表 低位横向
     * 13 代表 低位向上
     * <p/>
     * 21 代表 中位向下
     * 22 代表 中位横向
     * 23 代表 中位向上
     * <p/>
     * 31 代表 高位向下
     * 32 代表 高位横向
     * 33 代表 高位向上
     *
     * @param list
     * @return
     */
    public static int checkStochRSI(List<Double> list) {
        List<Integer> stochRSI = getStochRSI(list);
        int a1 = stochRSI.get(stochRSI.size() - 1);
        int a2 = stochRSI.get(stochRSI.size() - 2);
        int a3 = stochRSI.get(stochRSI.size() - 3);
        // num < 0 表示 下跌 =0 表示横向  >0 表示 上涨
        int num = Integer.compare(a1, a2) + Integer.compare(a1, a3) + Integer.compare(a2, a3);
        int way = Integer.compare(num, 0) + 2;

        int pos = a1 >= 80 ? 30 : (a1 <= 20 ? 10 : 20);
        return pos + way;
    }


    /**
     * LC := REF(CLOSE,1);
     * RSI:=SMA(MAX(CLOSE-LC,0),N,1)/SMA(ABS(CLOSE-LC),N,1) *100;
     * ％K:MA(RSI-LLV(RSI,M),P1)/MA(HHV(RSI,M)-LLV(RSI,M),P1)*100;
     * ％D:MA(％K,P2);
     *
     * @param list
     * @return
     */
    public static List<Integer> getStochRSI(List<Double> list) {
        int[] params = {14, 14, 3, 3};
        List<Double> REF_LIST = REF(list, 1);
        List<Double> MINUS_LIST = MINUS(list, REF_LIST);
        List<Double> RSI_LIST =  RATE(SMA(MAX(MINUS_LIST, 0D), params[0], 1), SMA(ABS(MINUS_LIST), params[0], 1));
        List<Double> LLV_LIST = LLV(RSI_LIST, params[1]);
        List<Double> HHV_LIST = HHV(RSI_LIST, params[1]);
        List<Double> K_LIST = RATE(MA(MINUS(RSI_LIST, LLV_LIST), params[2]), MA(MINUS(HHV_LIST, LLV_LIST), params[2]));
        List<Double> D_LIST = MA(K_LIST, params[3]);

        List<Integer> listRet = new ArrayList<>();
        for(Double d1:D_LIST)
        {
            listRet.add(d1.intValue());
        }
        printListReverse(listRet);
        return listRet;
    }

    public static void printListReverse(List list)
    {
        for(int m =list.size();m>0;m--)
        {
            System.out.print(list.get(m - 1) + ",");
        }
//        for(int m =1;m<=list.size();m++)
//        {
//            System.out.print(list.get(m - 1) + ",");
//        }
        System.out.println();
    }
}