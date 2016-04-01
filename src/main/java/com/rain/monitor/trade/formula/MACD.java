package com.rain.monitor.trade.formula;


import static com.rain.monitor.trade.formula.util.Functions.*;

import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MACD {

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
     *
     * EMA（12）= 前一日EMA（12）×11/13＋今日收盘价×2/13
     * EMA（26）= 前一日EMA（26）×25/27＋今日收盘价×2/27
     * DIFF=今日EMA（12）- 今日EMA（26）
     * DEA（MACD）= 前一日DEA×8/10＋今日DIF×2/10
     * BAR=2×(DIFF－DEA)
     *
     * @param list
     * @return
     */
    public static int checkMACD(List<Double> list) {
        int[] param = {12,26,9};
        List<Double> ema12 = EMA(list, param[0]);
        List<Double> ema26 = EMA(list, param[1]);
        List<Double> diff = MINUS(ema12, ema26);
        List<Double> dea = EMA(diff, param[2]);
        List<Double> bar = TIMES(MINUS(diff, dea), 2D);

        Double a1 = bar.get(bar.size() - 1);
        Double a2 = bar.get(bar.size() - 2);
        Double a3 = bar.get(bar.size() - 3);
        // num < 0 表示 下跌 =0 表示横向  >0 表示 上涨
        int num = Double.compare(a1, a2);
        int way = Integer.compare(num, 0) + 2;

        int pos = (a1>0 && a2>0 && a3 > 0)?30 :((a1<0 && a2<0 && a3 < 0)?10:20);
        return pos + way;
    }


}
