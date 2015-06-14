package com.example.entity;

public class EmailEntity {
	
	private String to;
	private String from;
	private String sentDate;
	private String subject;
	private String body;
	
	
	
	public EmailEntity(String to, String from, String sentDate, String subject,
			String body) {
		super();
		this.to = to;
		this.from = from;
		this.sentDate = sentDate;
		this.subject = subject;
		this.body = body;
	}
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSentDate() {
		return sentDate;
	}
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
}
