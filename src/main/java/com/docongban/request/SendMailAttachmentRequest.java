package com.docongban.request;

import lombok.Data;

@Data
public class SendMailAttachmentRequest {

	private String mailTitle;
	
	private String fullName;
	
	private String emailAddress;
	
	private String phoneNumber;
	
	private String pathAttachmentFile;
	
}
