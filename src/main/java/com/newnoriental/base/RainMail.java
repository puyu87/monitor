package com.newnoriental.base;


public class RainMail{

    static String smtp = "smtp.exmail.qq.com";
    static String from = "wangchenxi@okjiaoyu.cn";
    static String username="wangchenxi@okjiaoyu.cn";
    static String password="yisha009";
    /**
     * ���͸�ĳ��
     * @param to
     * @param subject
     * @param content
     * @return
     */
	public static boolean sendTo(String to,String subject,String content)
	{
		return Mail.sendAndCc(smtp, from, to,null, subject, content, username, password);
	}
	/**
	 * ���͸�ĳ�� ͬʱ����
	 * @param to
	 * @param copyTo
	 * @param subject
	 * @param content
	 * @return
	 */
	public static boolean sendTo(String to,String copyTo,String subject,String content)
	{
		return Mail.sendAndCc(smtp, from, to,copyTo, subject, content, username, password);
	}
	
    /**
     * ���͸�ĳ��
     * @param to
     * @param subject
     * @param content
     * @return
     */
	public static boolean sendToDefault(String subject,StringBuffer content)
	{
		return Mail.sendAndCc(smtp, from, defaultTo,copyTo, subject, content.toString(), username, password);
	}
	
	private static String defaultTo  = "chendengyu@okjiaoyu.cn";
	private static String copyTo = "wangchenxi@okjiaoyu.cn";

	public static void main(String[] args) {
		sendToDefault("�����ļ�����", new StringBuffer("�����ļ�����"));
	}
	
}
