package com.newnoriental.base;
  
import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;  
  
  
public class Mail {   

	
    private MimeMessage mimeMsg; //MIME�ʼ�����   
    private Session session; //�ʼ��Ự����   
    private Properties props; //ϵͳ����   
    //smtp��֤�û���������   
    private String username;   
    private String password; 
    private Multipart mp; //Multipart����,�ʼ�����,����,���������ݾ���ӵ����к�������MimeMessage����   
       
    /** 
     * Constructor 
     * @param smtp �ʼ����ͷ����� 
     */  
    public Mail(String smtp){   
        setSmtpHost(smtp);
        setSSLSecurity();
        createMimeMessage();   
    }   
  
    /** 
     * �����ʼ����ͷ����� 
     * @param hostName String  
     */  
    public void setSmtpHost(String hostName) {   
        System.out.println("����ϵͳ���ԣ�mail.smtp.host = " + hostName);
        if(props == null)  
            props = System.getProperties(); //���ϵͳ���Զ���    
        props.put("mail.smtp.host", hostName); //����SMTP����
    }   
  
  
    /** 
     * ����MIME�ʼ�����   
     * @return 
     */  
    public boolean createMimeMessage()   
    {   
        try {   
            System.out.println("׼����ȡ�ʼ��Ự����");
            session = Session.getInstance(props); //����ʼ��Ự����
        }   
        catch(Exception e){   
            System.err.println("��ȡ�ʼ��Ự����ʱ��������"+e);   
            return false;   
        }   
      
        System.out.println("׼������MIME�ʼ�����");   
        try {   
            mimeMsg = new MimeMessage(session); //����MIME�ʼ�����   
            mp = new MimeMultipart();   
          
            return true;   
        } catch(Exception e){   
            System.err.println("����MIME�ʼ�����ʧ�ܣ�"+e);   
            return false;   
        }   
    }     
      
    /** 
     * ����SMTP�Ƿ���Ҫ��֤ 
     * @param need 
     */  
    public void setNeedAuth(boolean need) {   
        System.out.println("����smtp�����֤��mail.smtp.auth = "+need);   
        if(props == null) props = System.getProperties();   
        if(need){   
            props.put("mail.smtp.auth","true");   
        }else{   
            props.put("mail.smtp.auth","false");   
        }   
    }

    public void setSSLSecurity(){
        String SSL_FACTORY="javax.net.ssl.SSLSocketFactory";

        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false") ;
        props.setProperty("mail.smtp.port","465") ;
        props.setProperty("mail.smtp.socketFactory.port","465") ;
        props.setProperty("mail.smtp.auth","true") ;
        props.put("mail.smtp.starttls.enable","true");
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // also use following for additional safety
        //props.put("mail.smtp.ssl.checkserveridentity", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

    }

    /** 
     * �����û��������� 
     * @param name 
     * @param pass 
     */  
    public void setNamePass(String name,String pass) {   
        username = name;   
        password = pass;   
    }   
  
    /** 
     * �����ʼ����� 
     * @param mailSubject 
     * @return 
     */  
    public boolean setSubject(String mailSubject) {   
        System.out.println("�����ʼ����⣡");   
        try{   
            mimeMsg.setSubject(mailSubject);   
            return true;   
        }   
        catch(Exception e) {   
            System.err.println("�����ʼ����ⷢ������");   
            return false;   
        }   
    }  
      
    /**  
     * �����ʼ����� 
     * @param mailBody String  
     */   
    public boolean setBody(String mailBody) {   
        try{   
            BodyPart bp = new MimeBodyPart();   
            bp.setContent(""+mailBody,"text/html;charset=GBK");   
            mp.addBodyPart(bp);   
          
            return true;   
        } catch(Exception e){   
        System.err.println("�����ʼ�����ʱ��������"+e);   
        return false;   
        }   
    }   
      
    /**  
     * ���÷����� 
     * @param from String  
     */   
    public boolean setFrom(String from) {   
        System.out.println("���÷����ˣ�");   
        try{   
            mimeMsg.setFrom(new InternetAddress(from)); //���÷�����   
            return true;   
        } catch(Exception e) {   
            return false;   
        }   
    }   
    /**  
     * ���������� 
     * @param to String  
     */   
    public boolean setTo(String to){   
        if(to == null)return false;   
        try{   
            mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));   
            return true;   
        } catch(Exception e) {   
            return false;   
        }     
    }   
      
    /**  
     * ���ó����� 
     * @param copyto String   
     */   
    public boolean setCopyTo(String copyto)   
    {   
        if(copyto == null)return false;   
        try{   
        mimeMsg.setRecipients(Message.RecipientType.CC,(Address[])InternetAddress.parse(copyto));   
        return true;   
        }   
        catch(Exception e)   
        { return false; }   
    }   
    /** 
     * ����sendOut��������ʼ����� 
     * @param smtp 
     * @param from 
     * @param to 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @return boolean 
     */  
    public static boolean send(String smtp,String from,String to,String subject,String content,String username,String password) {  
    	return sendAndCc(smtp,from,to,null,subject,content,username,password);
    }  
      
    /** 
     * ����sendOut��������ʼ�����,������ 
     * @param smtp 
     * @param from 
     * @param to 
     * @param copyto 
     * @param subject 
     * @param content 
     * @param username 
     * @param password 
     * @return boolean 
     */  
    public static boolean sendAndCc(String smtp,String from,String to,String copyto,String subject,String content,String username,String password) {  
        Mail theMail = new Mail(smtp);  
        theMail.setNeedAuth(true); //��Ҫ��֤  
          
        if(!theMail.setSubject(subject)) return false;  
        if(!theMail.setBody(content)) return false;  
        if(!theMail.setTo(to)) return false;  
        if(copyto != null)
        {
        	if(!theMail.setCopyTo(copyto)) return false;  
        }
        if(!theMail.setFrom(from)) return false;  
        theMail.setNamePass(username,password);  
          
        if(!theMail.sendOut()) return false;  
        return true;  
    }  
     
    /**  
     * �����ʼ� 
     */   
    private boolean sendOut()   
    {   
        try{   
            mimeMsg.setContent(mp);   
            mimeMsg.saveChanges();   
            System.out.println("���ڷ����ʼ�....");   
              
            Session mailSession = Session.getInstance(props,null);   
            Transport transport = mailSession.getTransport("smtp");   
            transport.connect((String) props.get("mail.smtp.host"), username, password);
            transport.sendMessage(mimeMsg,mimeMsg.getAllRecipients());
            System.out.println("�����ʼ��ɹ���");
            transport.close();   
              
            return true;   
        } catch(Exception e) {   
            System.err.println("�ʼ�����ʧ�ܣ�"+e);   
            e.printStackTrace();
            return false;   
        }   
    }   
}   