package com.aug3.yhyc.mail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aug3.yhyc.util.ConfigManager;

public class MailSender {

	private static MailSender sender = null;

	private String host = ConfigManager.getProperties().getProperty("mail.host", "smtp.163.com");
	private String port = ConfigManager.getProperties().getProperty("mail.port", "25");
	private boolean authenticate = ConfigManager.getProperties().getBooleanProperty("mail.user.authenticate", true);
	private String from = ConfigManager.getProperties().getProperty("mail.user.name", "yhyc_admin@163.com");
	private String pwd = ConfigManager.getProperties().getProperty("mail.user.pwd", "163.com");

	private ExecutorService executor = Executors.newCachedThreadPool();

	private MailSender() {
	}

	public static MailSender getInstance() {
		if (sender == null) {
			sender = new MailSender();
		}

		return sender;
	}

	public void send(final MailBody mailBody) {

		executor.execute(new Runnable() {

			@Override
			public void run() {
				// 这个类主要是设置邮件
				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setMailServerHost(host);
				mailInfo.setMailServerPort(port);
				mailInfo.setAuthenticate(authenticate);
				mailInfo.setUserName(from); // 实际发送者
				mailInfo.setPassword(pwd);// 您的邮箱密码
				mailInfo.setFromAddress(from); // 设置发送人邮箱地址
				mailInfo.setToAddress(mailBody.getMailto()); // 设置接受者邮箱地址
				mailInfo.setSubject(mailBody.getSubject());
				mailInfo.setContent(mailBody.getContent());// "内容<a>http://www.xiaomi.com</a>"

				SimpleMailSender sms = new SimpleMailSender();

				if (mailBody.isHtml()) {
					sms.sendHtmlMail(mailInfo);
				} else {
					sms.sendTextMail(mailInfo);
				}
			}
		});
	}
}
