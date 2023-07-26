package com.docongban.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.docongban.request.SendMailAttachmentRequest;
import com.docongban.service.EmailService;

@Component
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Value("${admin.email}")
	private String adminEmail;

	@Value("${apply.content-mail}")
	private String contentMail;

	/**
	 * Send mail: text.
	 * 
	 * @param to      receiver email
	 * @param subject mail title
	 * @param text    mail content
	 */
	@Override
	public void sendSimpleMessage(String to, String subject, String text) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("noreply@baeldung.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}

	/**
	 * Send mail with attachment.
	 * 
	 * @param to               receiver email
	 * @param subject          mail title
	 * @param text             mail content
	 * @param pathToAttachment path to attachment
	 * @throws MessagingException
	 */
	@Override
	public void sendMailWithAttachment(SendMailAttachmentRequest request) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		FileSystemResource attachmentFile = new FileSystemResource(request.getPathAttachmentFile());

		helper.setFrom(request.getEmailAddress());
		helper.setTo(adminEmail);
		helper.setSubject(request.getMailTitle());
		helper.setText(contentMail);
		helper.addAttachment(attachmentFile.getFilename(), attachmentFile);

		emailSender.send(message);
	}
}
