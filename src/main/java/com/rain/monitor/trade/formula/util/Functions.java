package com.rain.monitor.trade.formula.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 */
public class Functions {
    /**
     * ���� Inde ��ֵ
     * @param list
     * @param  days
     * @return
     */
    public static List<Double> REF(List<Double> list,int days)
    {
        ArrayList<Double> objects = new ArrayList<>(list.size());
        if(list.size() > 0)
        {
            for(int n=0;n<days;n++)
            {
                objects.add(list.get(0));
            }
            for(int m=0;m<list.size()-days;m++)
            {
                objects.add(list.get(m));
            }
        }
        return objects;
    }
    /**
     * ���� ���ֵ
     * @param d1
     * @param d2
     * @return
     */
    public static List<Double> MAX(List<Double> d1,List<Double> d2)
    {
        ArrayList<Double> objects = new ArrayList<>(d1.size());
        if(d1.size() != d2.size())
        {
            throw new RuntimeException("�Ƚϳ��Ȳ�һ��");
        }
        for (int m=0;m<d1.size();m++)
        {
            objects.add(Math.max(d1.get(m), d2.get(m)));
        }
        return objects;
    }
    /**
     * ���� ���ֵ
     * @param d1
     * @param value
     * @return
     */
    public static List<Double> MAX(List<Double> d1,Double value)
    {
        ArrayList<Double> objects = new ArrayList<>(d1.size());
        for (int m=0;m<d1.size();m++)
        {
            objects.add(Math.max(d1.get(m), value));
        }
        return objects;
    }
    /**
     * ���� ����ֵ
     * @param d1
     * @return
     */
    public static List<Double> ABS(List<Double> d1)
    {
        ArrayList<Double> objects = new ArrayList<>(d1.size());
        for (int m=0;m<d1.size();m++)
        {
            objects.add(Math.abs(d1.get(m)));
        }
        return objects;
    }

    /**
     * SMA ���㷽�� ��
     * list ֮ǰ�� N�� ����ƽ���� weight���� ���� ��Ȩ��
     * @param list
     * @param days
     * @param weight �����Ȩ��
     */
    public static List<Double> SMA(List<Double> list,int days,int weight)
    {

        ArrayList<Double> objects = new ArrayList<>(list.size());
        Double sma = 0D;
        for (int m=0;m<list.size();m++)
        {
            sma = (list.get(m)*weight + sma*(days-weight))/days;
            objects.add(sma);
        }
        return objects;
    }

    /**
     * EMA ���㷽�� ��
     *
     * @param list
     * @param days
     */
    public static List<Double> EMA(List<Double> list,int days)
    {
        ArrayList<Double> objects = new ArrayList<>(list.size());

        Double ema = 0D;
        for (int m=0;m<list.size();m++)
        {
            ema = (  ema*(days-1)  +  list.get(m)*2 ) / (days+1);
            objects.add(ema);
            if(m==0)
            {
                ema = list.get(m);
            }
        }
        return objects;
    }
    /**
     * MA ���㷽�� ��
     * list ֮ǰ�� N�� ����ƽ����
     * @param list
     * @param days
     */
    public static List<Double> MA(List<Double> list,int days)
    {
        ArrayList<Double> objects = new ArrayList<>(list.size());
        int index = 0;
        Double sum = 0D;
        for(int n = 0;n<list.size();n++)
        {
            sum += list.get(n);
            if(n >= days)
            {
                sum = sum - list.get(index);
                index++;
            }
            objects.add(sum/(n-index+1));
        }
        return objects;
    }

    /**
     * ���� n ����  ��Сֵ
     * @param list
     * @param days
     * @return
     */
    public static List<Double> LLV(List<Double> list,int days)
    {
        ArrayList<Double> objects = new ArrayList<>(list.size());

        for(int n = 0;n<list.size();n++)
        {
            int start = Math.max(0,n-days+1);
            Double min = list.get(start);
            for(int m=start+1; m<=n; m++ )
            {
                min = Math.min(min,list.get(m));
            }
            objects.add(min);
        }
        return objects;
    }

    public static void main(String[] args) {
        List<Double> doubles = Arrays.asList(1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D, 9D);
        List<Double> llv = LLV(doubles, 3);
        System.out.println(llv);
    }

    /**
     * ���� n ����  ���ֵ
     * @param list
     * @param days
     * @return
     */
    public static List<Double> HHV(List<Double> list,int days)
    {
        ArrayList<Double> objects = new ArrayList<>(list.size());

        for(int n = 0;n<list.size();n++)
        {
            int start = Math.max(0,n-days+1);
            Double max = list.get(start);
            for(int m=start+1; m<=n; m++ )
            {
                max = Math.max(max, list.get(m));
            }
            objects.add(max);
        }
        return objects;
    }

    /**
     * ����ٷֱ�
     * @param list1
     * @param list2
     * @return
     */
    public static List<Double> RATE(List<Double> list1,List<Double> list2)
    {
        if(list1.size() != list2.size())
        {
            throw new RuntimeException("���ݳ��Ȳ�һ�£�");
        }

        ArrayList<Double> objects = new ArrayList<>(list1.size());

        for(int n = 0;n<list1.size();n++)
        {
            Double result = 0D;
            if(list2.get(n) == 0)
            {
                result = 100D;
            }else
            {
                result = list1.get(n) /list2.get(n) *100;
            }
            objects.add(result);
        }
        return objects;
    }

    /**
     * �����
     * @param list1
     * @param list2
     * @return
     */
    public static List<Double> MINUS(List<Double> list1,List<Double> list2)
    {
        if(list1.size() != list2.size())
        {
            throw new RuntimeException("���ݳ��Ȳ�һ�£�");
        }

        ArrayList<Double> objects = new ArrayList<>(list1.size());

        for(int n = 0;n<list1.size();n++)
        {
            objects.add(list1.get(n) - list2.get(n));
        }
        return objects;
    }
    /**
     * ���� ����
     * @param list1
     * @param times
     * @return
     */
    public static List<Double> TIMES(List<Double> list1,Double times)
    {

        ArrayList<Double> objects = new ArrayList<>(list1.size());

        for(int n = 0;n<list1.size();n++)
        {
            objects.add(list1.get(n) * times);
        }
        return objects;
    }

    /**
     * ��ӡ�б�
     * @param list
     */
    public static void printList(List list)
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
