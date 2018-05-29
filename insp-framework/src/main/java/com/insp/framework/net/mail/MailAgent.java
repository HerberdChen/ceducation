package com.insp.framework.net.mail;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insp.framework.utility.date.DateTime;
import com.sun.mail.imap.IMAPStore;

/**
 * 邮箱代理
 * @author admin
 *
 */
public class MailAgent {
	/** 类logger */
	private static Logger commonlogger = LoggerFactory.getLogger(MailAgent.class); ;
	/** 对象logger */
	private Logger logger = null;
	/** 发送服务器 */
	private String sendhost;
	/** 接收服务器 */
	private String recvhost;
	/** 邮箱账号 */
	private String username;
	/** 邮箱接收邮件认证密码 */
	private String poppassword;
	/** 邮箱发送邮件认证密码 */
	private String imappassword;
	
	private String recvType;
	private String recvserver;
	private String recvport = "995";
	private String sendserver;
	private String sendport;
	private String password;
	private Properties props = new Properties();
	
	/** 
	 * 邮箱配置,用于存放mail.properties中取得邮箱配置信息
	 * key为邮箱ID，value为数组，分别存放发送服务器地址，接收服务器地址，邮箱账号，邮箱发送认证密码,邮箱接收认证密码 
	 **/
	public static Map<String,String[]> DEFAULTS = new HashMap<String,String[]>();
	/**
	 * 静态初始化，用于读取邮箱配置
	 */
	static {
		//读取mail.properties取得邮箱配置信息
		Properties props = new Properties();
		try {
			props.load(MailAgent.class.getResourceAsStream("/mail.properties"));
			for(Object key : props.keySet()) {
				if(!key.toString().startsWith("mail."))
					continue;
				String mailid = key.toString().substring(key.toString().indexOf(".")+1, key.toString().length());
				String value = props.getProperty(key.toString());
				String[] values = value.split(",");
				if(values == null || values.length!=5) {
					commonlogger.error("邮箱配置项无效:"+key.toString()+"="+value);
					continue;
				}
				DEFAULTS.put(mailid, values);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 缺省构造方法
	 * 将读取mail.properties中的第一个配置项
	 */
	public MailAgent() {
		if(DEFAULTS.size()<=0)
			return;
		for(String key : DEFAULTS.keySet()) {
			String[] values = DEFAULTS.get(key);
			sendhost = values[0];
			recvhost = values[1];
			username = values[2];
			imappassword = values[3];
			poppassword = values[4];
			break;
		}
		logger = LoggerFactory.getLogger(username);
		logger.info("设定邮箱："+username);
		init();
	}
	/**
	 * 根据取mail.properties取得邮箱信息
	 * @param mailid mail.properties中的key
	 */
	public MailAgent(String mailid) {
		if(!DEFAULTS.containsKey(mailid)) {
			commonlogger.error("无效邮箱ID："+mailid);
			return;
		}
		String[] values = DEFAULTS.get(mailid);
		sendhost = values[0];
		recvhost = values[1];
		username = values[2];
		imappassword = values[3];
		poppassword = values[4];
		
		logger = LoggerFactory.getLogger(username);
		logger.info("设定邮箱："+username);
		init();
	}
	/**
	 * 构造方法
	 * @param host 
	 * @param username
	 * @param password
	 */
	public MailAgent(String sendhost,String recvhost,String username,String recvpassword,String sendpassword) {
		this.sendhost = sendhost;
		this.recvhost = recvhost;
		this.username = username;
		this.poppassword = recvpassword;
		this.imappassword = sendpassword;
		
		logger = LoggerFactory.getLogger(username);
		logger.info("设定邮箱："+username);
		init();
	}
	
	private void init() {
		recvserver = this.recvhost;
		recvport = "995";
		if(this.recvhost.contains(":")) {
			String[] ss = recvhost.split(":");
			if(ss != null && ss.length>=3) {
				recvType=ss[0];
				recvserver = ss[1];
				recvport = ss[2];
			}else if(ss!=null && ss.length>=2) {
				recvserver = ss[0];
				recvport = ss[1];
			}
		}
		if(recvType==null || recvType.equals("")) {
			if(recvserver.contains("pop"))
				recvType = "pop3";
			else
				recvType = "imap";
			
		}
		sendserver = this.sendhost;
		sendport = "465";
		if(this.sendhost.contains(":")) {
			String[] ss = sendhost.split(":");
			if(ss != null && ss.length>=2) {
				sendserver = ss[0];
				sendport = ss[1];
			}
		}
		
		
		props = new Properties();
		
		if(recvType.equalsIgnoreCase("pop3")) {	
			password = poppassword;
			props.setProperty("mail.pop3.host", recvserver); // 按需要更改
			props.setProperty("mail.pop3.port", recvport+"");
	        // SSL安全连接参数
			props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.pop3.socketFactory.fallback", "true");
			props.setProperty("mail.pop3.socketFactory.port", recvport+"");
		}else {
			password = imappassword;
			props.setProperty("mail.store.protocol", "imap"); 
	        props.setProperty("mail.imap.host", recvserver); 
	        props.setProperty("mail.imap.port", recvport); 	
	        
	        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
	        props.setProperty("mail.imap.socketFactory.fallback", "false");
            props.setProperty("mail.imap.socketFactory.port",recvport);
            
            props.setProperty("mail.imap.class", "com.sun.mail.imap.IMAPStore"); 
     
            props.setProperty("mail.imap.auth.login.disable", "true"); 

		}
		props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", this.sendserver);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        
        final String smtpPort = sendport;
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);   
        props.setProperty("mail.smtp.auth", "true");
	}
	public List<Mail> doReceive(String lastArriveTime) throws MessagingException {
		List<Mail> mails = new ArrayList<Mail>();

        logger.info("开始连接邮件服务器("+this.recvhost+")..."); 
        //MailAuthenticator auth = new MailAuthenticator(username, password);
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore(recvType);
        store.connect(recvserver, username,password);
        
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        Message message[] = folder.getMessages();  
        // 打印不同状态的邮件数量 
        logger.info("收件箱中共" + (message==null?0:message.length) + "封邮件!"); 
        logger.info("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!"); 
        logger.info("收件箱中共" + folder.getNewMessageCount() + "封新邮件!"); 
        logger.info("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!"); 
        //logger.info("接收邮件数量:　" + (message==null?0:message.length));
        
        logger.info("邮件过滤(保留"+lastArriveTime+"之后)...");
        for(Message m : message) {
			Date d;
			try {
				d = m.getReceivedDate();
				if(d == null)
					d = m.getSentDate();
				if(DateTime.format(d).compareTo(lastArriveTime)<=0)
					continue;
				mails.add(new Mail((MimeMessage) m));
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}			
		}
        return mails;
	}
	
	public void sendMail(String sender,String receiver,String title,String content) throws Exception{
		// 1. 创建一封邮件                
		Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);     // 创建邮件对象

        // 1. From: 发件人
        //    其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
        //    真正要发送时, 邮箱必须是真实有效的邮箱。
        message.setFrom(new InternetAddress(sender, "", "UTF-8"));

        // 2. To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver, "", "UTF-8"));
        //    To: 增加收件人（可选）
        //message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("dd@receive.com", "USER_DD", "UTF-8"));
        //    Cc: 抄送（可选）
        //message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("ee@receive.com", "USER_EE", "UTF-8"));
        //    Bcc: 密送（可选）
        //message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("ff@receive.com", "USER_FF", "UTF-8"));

        // 3. Subject: 邮件主题
        message.setSubject(title, "UTF-8");

        // 4. Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");

        // 5. 设置显示的发件时间
        message.setSentDate(new Date());

        // 6. 保存前面的设置
        message.saveChanges();

        // 7. 根据配置创建会话对象, 用于和邮件服务器交互
       
        //session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log



        // 8. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();

        // 9. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        transport.connect(this.username, this.password);

        // 10. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 11. 关闭连接
        transport.close();
	}
	public String getSendhost() {
		return sendhost;
	}
	public void setSendhost(String sendhost) {
		this.sendhost = sendhost;
	}
	public String getRecvhost() {
		return recvhost;
	}
	public void setRecvhost(String recvhost) {
		this.recvhost = recvhost;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRecvpassword() {
		return poppassword;
	}
	public void setRecvpassword(String recvpassword) {
		this.poppassword = recvpassword;
	}
	public String getSendpassword() {
		return imappassword;
	}
	public void setSendpassword(String sendpassword) {
		this.imappassword = sendpassword;
	}
	public String getRecvserver() {
		return recvserver;
	}
	public void setRecvserver(String recvserver) {
		this.recvserver = recvserver;
	}
	public String getRecvport() {
		return recvport;
	}
	public void setRecvport(String recvport) {
		this.recvport = recvport;
	}
	public String getSendserver() {
		return sendserver;
	}
	public void setSendserver(String sendserver) {
		this.sendserver = sendserver;
	}
	public String getSendport() {
		return sendport;
	}
	public void setSendport(String sendport) {
		this.sendport = sendport;
	}
	public Properties getProps() {
		return props;
	}
}
