package com.docongban.service;

import javax.mail.MessagingException;

import com.docongban.request.SendMailAttachmentRequest;

public interface EmailService {
	void sendSimpleMessage(String to,String subject, String text);
	
	void sendMailWithAttachment(SendMailAttachmentRequest request) throws MessagingException;
}
