package com.aug3.yhyc.mail;

public class MailBody {

	private String mailto;

	private String subject;

	private String content;

	private boolean html = true;

	private boolean hasAttach;

	// attachment name
	private String afn;

	// attachment file path
	private String afp;

	public String getMailto() {
		return mailto;
	}

	public void setMailto(String mailto) {
		this.mailto = mailto;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}

	public boolean isHasAttach() {
		return hasAttach;
	}

	public void setHasAttach(boolean hasAttach) {
		this.hasAttach = hasAttach;
	}

	public String getAfn() {
		return afn;
	}

	public void setAfn(String afn) {
		this.afn = afn;
	}

	public String getAfp() {
		return afp;
	}

	public void setAfp(String afp) {
		this.afp = afp;
	}

}
