package com.docongban.service.impl;

import javax.mail.MessagingException;
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
	private static String ADMIN_EMAIL;

	@Value("${apply.content-mail}")
	private static String CONTENT_MAIL;

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

//		FileSystemResource fileResource = new FileSystemResource(request.getFileAttachment());
//		String fileName = file.getFilename();

		helper.setFrom(request.getEmailAddress());
		helper.setTo(ADMIN_EMAIL);
		helper.setSubject(request.getMailTitle());
		helper.setText(CONTENT_MAIL);
		helper.addAttachment(request.getFileAttachment().getOriginalFilename(), request.getFileAttachment());

		emailSender.send(message);
	}
	
//	private void getFile(MultipartFile multipartFile) throws IOException {
//		Path staticPath = Paths.get("src/main/resources/static");
//        Path imagePath = Paths.get("attachment");
//        // Kiểm tra tồn tại hoặc tạo thư mục /static/attachment
//        if (!Files.exists(Constants.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
//            Files.createDirectories(Constants.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
//        }
//        Path file = Constants.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(multipartFile.getOriginalFilename());
//        try (OutputStream os = Files.newOutputStream(file)) {
//            os.write(multipartFile.getBytes());
//        }
//	}
}
