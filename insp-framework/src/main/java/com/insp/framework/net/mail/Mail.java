package com.insp.framework.net.mail;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.insp.framework.utility.date.DateTime;
import com.insp.framework.utility.io.FileUtils;
import com.insp.framework.utility.io.PathUtils;

public class Mail {
	private MimeMessage mimeMessage = null;	
    private List<StringBuffer> bodytext = new ArrayList<StringBuffer>();//存放邮件内容
    private Map<String,InputStream> attaches = new HashMap<String,InputStream>();
    
    Mail(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }
    @Override
    public String toString() {
    	return this.getFrom()+":"+this.getSubject()+"("+this.getSentDateStr()+")";
    }

    /**
     * 获得发件人的地址和姓名
     * @return 格式：personal + "<" + from + ">";
     */
    public String getSendInfo() {
        InternetAddress address[];
		try {
			address = (InternetAddress[]) mimeMessage.getFrom();
			String from = address[0].getAddress();
			if (from == null)
				from = "";
			String personal = address[0].getPersonal();
			if (personal == null)
				personal = "";
			String fromaddr = personal + "<" + from + ">";
			return fromaddr;	
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
       
    }
    
    /**
     * 获得发件人的地址
     * @return
     */
    public String getFrom() {
        InternetAddress address[];
		try {
			address = (InternetAddress[]) mimeMessage.getFrom();
			String from = address[0].getAddress();
			if (from == null)
				from = "";
			return from;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
       
    }

    /**
     * 获得邮件的收件人，抄送，和密送的地址和姓名
     * @param type  "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址
     * @return
     * @throws Exception
     */
    public String getMailAddress(String type){
    	try {
	        String mailaddr = "";
	        String addtype = type.toUpperCase();
	        InternetAddress[] address = null;
	        if (addtype.equals("") || addtype.equalsIgnoreCase("all") || addtype.equalsIgnoreCase("TO") || addtype.equalsIgnoreCase("CC") || addtype.equalsIgnoreCase("BCC")) {
	            if (addtype.equals("") || addtype.equalsIgnoreCase("all") || addtype.equalsIgnoreCase("TO")) {
	                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
	            } else if (addtype.equals("") || addtype.equalsIgnoreCase("all") || addtype.equalsIgnoreCase("CC")) {
	                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
	            } else {
	                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
	            }
	            if (address != null) {
	                for (int i = 0; i < address.length; i++) {
	                    String email = address[i].getAddress();
	                    if (email == null)
	                        email = "";
	                    else {
	                        email = MimeUtility.decodeText(email);
	                    }
	                    String personal = address[i].getPersonal();
	                    if (personal == null)
	                        personal = "";
	                    else {
	                        personal = MimeUtility.decodeText(personal);
	                    }
	                    String compositeto = personal + "<" + email + ">";
	                    mailaddr += "," + compositeto;
	                }
	                mailaddr = mailaddr.substring(1);
	            }
	        } else {
	            throw new Exception("Error emailaddr type!");
	        }
	        return mailaddr;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return "";
    	}
    }

    /**
     * 获得邮件主题
     */
    public String getSubject()  {
        String subject = "";
        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
            if (subject == null)
                subject = "";
        } catch (Exception exce) {
        	exce.printStackTrace();
        }
        return subject;
    }

    /**
     * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
     */
    public boolean getReplySign(){
    	
        boolean replysign = false;
        try {
	        String needreply[] = mimeMessage
	                .getHeader("Disposition-Notification-To");
	        if (needreply != null) {
	            replysign = true;
	        }
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return replysign;
    }

    /**
     * 获得此邮件的Message-ID
     */
    public String getMessageId() {
        try {
			return mimeMessage.getMessageID();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    }

    /**
     * 【判断此邮件是否已读，如果未读返回返回false,反之返回true】
     */
    public boolean isNew() {
    	try {
	        boolean isnew = false;
	        Flags flags = ((Message) mimeMessage).getFlags();
	        Flags.Flag[] flag = flags.getSystemFlags();
	        //System.out.println("flags's length: " + flag.length);
	        for (int i = 0; i < flag.length; i++) {
	            if (flag[i] == Flags.Flag.SEEN) {
	                isnew = true;
	                //System.out.println("seen Message.......");
	                break;
	            }
	        }
	        return isnew;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }

    public Date getSentDate() {

		try {
			return mimeMessage.getSentDate();		
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
       
    }

    /**
     * 获得邮件发送日期
     */
    public String getSentDateStr() {

		try {
			Date sentdate = mimeMessage.getSentDate();
			 return DateTime.format(sentdate);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
       
    }
    
    public Date getReceivedDate() {

		try {
			Date d = mimeMessage.getReceivedDate();
			 return d;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
       
    }
    public String getReceivedDateStr() {

		try {
			Date d = mimeMessage.getReceivedDate();
			 return DateTime.format(d);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
       
    }
    
    

    public MimeMessage getMimeMessage() {
        return mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }
    public String getMailContentText() throws Exception{
    	getMailContent();
    	Optional<String> option = bodytext.stream().map(x->x.toString()).reduce((a,b)->a+"\n"+b);
    	return option.isPresent()?option.get():"";
    	
    }
    public List<StringBuffer> getMailContent()  throws Exception{
    	Part part = this.mimeMessage;
    	this.bodytext.clear();
    	this.getMailContent(part);
    	return this.bodytext;
    }
    private void getMailContent(Part part) throws Exception {    	
        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if (nameindex != -1)
            conname = true;
        //System.out.println("CONTENTTYPE: " + contenttype);
        if (part.isMimeType("text/plain") && !conname) {
            bodytext.add(new StringBuffer((String) part.getContent()));            
        } else if (part.isMimeType("text/html") && !conname) {
        	bodytext.add(new StringBuffer((String) part.getContent()));            
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContent(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent());
        } else {
        }
    }

    public List<StringBuffer> getBodytext() throws Exception{
    	if(this.bodytext.size()<=0)
    		this.getMailContent();
        return bodytext;
    }
    
    public boolean isContainAttach() {
    	Part part = this.mimeMessage;
    	try {
			return isContainAttach(part);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	
    }
    /**
     * 判断此邮件是否包含附件
     */
    private boolean isContainAttach(Part part) throws Exception {
        boolean attachflag = false;
        String contentType = part.getContentType();
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE))))
                    attachflag = true;
                else if (mpart.isMimeType("multipart/*")) {
                    attachflag = isContainAttach((Part) mpart);
                } else {
                    String contype = mpart.getContentType();
                    if (contype.toLowerCase().indexOf("application") != -1)
                        attachflag = true;
                    if (contype.toLowerCase().indexOf("name") != -1)
                        attachflag = true;
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            attachflag = isContainAttach((Part) part.getContent());
        }
        return attachflag;
    }

    public Map<String,InputStream> getAttachMent() throws Exception{
    	attaches.clear();
		return getAttachMent(this.mimeMessage);
    	
    }
    
    /**
     *  getInstance().getMailContent((Part) message[i], pmm);
            String content = pmm.getBodyText().substring(pmm.getBodyText().indexOf("<"), pmm.getBodyText().lastIndexOf(">") + 1);
            System.out.println(" 内容: \r\n" + content);
            pmm.setAttachPath(fileSavePath);
            getInstance().saveAttachMent((Part) message[i], pmm);
     * 【保存附件】
     */
    public Map<String,InputStream> getAttachMent(Part part) throws Exception {
        String fileName = "";
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart mpart = mp.getBodyPart(i);
                String disposition = mpart.getDisposition();
                if ((disposition != null)
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE)))) {
                    fileName = mpart.getFileName();
                    if (fileName.toLowerCase().indexOf("gb") != -1) {
                        fileName = MimeUtility.decodeText(fileName);
                    }
                    attaches.put(fileName, mpart.getInputStream());                    
                } else if (mpart.isMimeType("multipart/*")) {
                	getAttachMent(mpart);
                } else {
                    fileName = mpart.getFileName();
                    if ((fileName != null)
                            && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
                        fileName = MimeUtility.decodeText(fileName);
                        attaches.put(fileName, mpart.getInputStream());                          
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
        	getAttachMent((Part) part.getContent());
        }
        
        return attaches;
    }
    
    public void saveMailToFile(String filenamePrefix) throws Exception{
    	String mailAuth = getSendInfo();
		String sendDate = getSentDateStr();
		String recvDate = getReceivedDateStr();
		String toAddress = getMailAddress("");
		String title = getSubject();
		boolean autoreply = getReplySign();
		boolean isNew = isNew();
		String content = getMailContentText();
		String decodecontent = MimeUtility.decodeText(content);
		
		Map<String,InputStream> attaches = getAttachMent();
		String attachesNames = attaches==null||attaches.size()<=0?"":attaches.keySet().stream().reduce((a,b)->a+","+b).get();
		
		String basicfile = filenamePrefix+".txt";

		
		decodecontent = "发信人:"+mailAuth+
				        "\n标题:"+title+				        
				        "\n发送时间:"+sendDate+
				        "\n附件:"+attachesNames+
				        "\n接收时间:"+recvDate+
				        "\n收信人:"+toAddress+
				        "\n是否回执:"+autoreply+
				        "\n正文:\n"+decodecontent;		
		FileUtils.writeText(decodecontent,basicfile);
		
		if(attaches == null || attaches.size()<=0)return;
		
		
		PathUtils.createDir(filenamePrefix);
		
		for(String attach : attaches.keySet()) {			
			InputStream attachContent = attaches.get(attach);
			String filename = filenamePrefix + "\\" + attach;
			FileUtils.save(attachContent, filename);			
		}
		
    }
   
}
