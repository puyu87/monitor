package com.rain.monitor.trade.formula;


import static com.rain.monitor.trade.formula.util.Functions.*;

import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 */
public class MACD {

    /**
     * ���� �Ƿ� stochRsi
     * 11 ���� ��λ����
     * 12 ���� ��λ����
     * 13 ���� ��λ����
     * <p/>
     * 21 ���� ��λ����
     * 22 ���� ��λ����
     * 23 ���� ��λ����
     * <p/>
     * 31 ���� ��λ����
     * 32 ���� ��λ����
     * 33 ���� ��λ����
     *
     *
     * EMA��12��= ǰһ��EMA��12����11/13���������̼ۡ�2/13
     * EMA��26��= ǰһ��EMA��26����25/27���������̼ۡ�2/27
     * DIFF=����EMA��12��- ����EMA��26��
     * DEA��MACD��= ǰһ��DEA��8/10������DIF��2/10
     * BAR=2��(DIFF��DEA)
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
        // num < 0 ��ʾ �µ� =0 ��ʾ����  >0 ��ʾ ����
        int num = Double.compare(a1, a2);
        int way = Integer.compare(num, 0) + 2;

        int pos = (a1>0 && a2>0 && a3 > 0)?30 :((a1<0 && a2<0 && a3 < 0)?10:20);
        return pos + way;
    }


}
