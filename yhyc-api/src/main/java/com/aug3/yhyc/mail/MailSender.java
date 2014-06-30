package com.aug3.yhyc.mail;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aug3.sys.util.DateUtil;
import com.aug3.yhyc.util.ConfigManager;

public class MailSender {

	private static MailSender sender = null;

	private String host = ConfigManager.getProperties().getProperty(
			"mail.host", "smtp.163.com");
	private String port = ConfigManager.getProperties().getProperty(
			"mail.port", "25");
	private boolean authenticate = ConfigManager.getProperties()
			.getBooleanProperty("mail.user.authenticate", true);
	private String from = ConfigManager.getProperties().getProperty(
			"mail.user.name", "yhyc_admin@163.com");
	private String pwd = ConfigManager.getProperties().getProperty(
			"mail.user.pwd", "163.com");

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
				mailInfo.setContent(mailBody.getContent());

				SimpleMailSender sms = new SimpleMailSender();

				if (mailBody.isHtml()) {
					sms.sendHtmlMail(mailInfo);
				} else {
					sms.sendTextMail(mailInfo);
				}
			}
		});
	}

	public void sendPasswordNotification(String mail, long t) {

		MailBody m = new MailBody();
		m.setMailto(mail);
		m.setSubject(ConfigManager.getProperties().getProperty(
				"mail.tpl.subject.chgpwd", "hello from 1hyc.com"));
		m.setHtml(true);
		String url = "http://api.1hyc.com/yhyc-api/services/user/password/confirm?mail="
				+ mail + "&key=" + t;
		// 亲爱的1号云仓用户，<br><br>您于@time@修改了登录密码， 请在12小时内进行确认：<a
		// href="@url@">@url@</a><br><br>谢谢,<br>1号云仓，您身边的菜场，坚持35天健康饮食，改变从这里开始
		m.setContent(ConfigManager
				.getProperties()
				.getProperty("mail.tpl.content.chgpwd")
				.replaceAll(
						"@time@",
						DateUtil.formatDate(new Date(),
								DateUtil.ISO_TIMESTAMP_PATTERN))
				.replaceAll("@url@", url));

		send(m);
	}
}
