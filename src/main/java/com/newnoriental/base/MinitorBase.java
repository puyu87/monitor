package com.newnoriental.base;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public abstract class MinitorBase{
    //创建 线程池
    protected static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    public abstract  void execute();

    public static void main(String[] args){
        new Sequence48Minitor().execute();
        new Sequence242Minitor().execute();
    }

    public static void sendToMain(String mailTo,String title,String content)
    {
        RainMail.sendTo(mailTo, title, content);
    }

    public void println(String content) {
        System.out.println(getClass().getSimpleName() + ">> " + content);
    }
}
