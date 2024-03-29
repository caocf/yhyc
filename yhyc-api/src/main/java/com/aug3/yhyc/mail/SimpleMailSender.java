package com.aug3.yhyc.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class SimpleMailSender {

	private static final Logger logger = Logger.getLogger(SimpleMailSender.class);

	private final static String CONTENT_ENCODING = "text/html; charset=utf-8";

	private Message initiateMailMessage(MailSenderInfo mailInfo) throws MessagingException {

		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties props = mailInfo.getProperties();
		if (mailInfo.isAuthenticate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(props, authenticator);

		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		try {
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);

			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);

			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());

			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());

			return mailMessage;

		} catch (MessagingException ex) {
			throw ex;
		}

	}

	/**
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public boolean sendTextMail(MailSenderInfo mailInfo) {

		try {
			Message mailMessage = initiateMailMessage(mailInfo);

			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);

			// 发送邮件
			Transport.send(mailMessage);
			return true;

		} catch (MessagingException ex) {
			logger.error(ex.getMessage());
		}

		return false;

	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public boolean sendHtmlMail(MailSenderInfo mailInfo) {

		try {
			Message mailMessage = initiateMailMessage(mailInfo);

			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart multipart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), CONTENT_ENCODING);
			multipart.addBodyPart(html);

			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(multipart);
			mailMessage.saveChanges();

			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 * @param fn
	 *            "apidocument.txt"
	 * @param fp
	 *            "app/api document.txt"
	 * @return
	 */
	public boolean sendHtmlMail(MailSenderInfo mailInfo, String fn, String fp) {

		try {
			Message mailMessage = initiateMailMessage(mailInfo);

			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart multipart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), CONTENT_ENCODING);
			multipart.addBodyPart(html);

			// 设置信件的附件(用本地上的文件作为附件)
			html = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(fp);
			DataHandler dh = new DataHandler(fds);
			html.setFileName(fn);
			html.setDataHandler(dh);
			multipart.addBodyPart(html);

			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(multipart);
			mailMessage.saveChanges();

			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}
}