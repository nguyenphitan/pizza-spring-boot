package com.docongban.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface CommonUploadFileService {
	
	/**
	 * Common upload file.
	 * 
	 * @param file attachment
	 * 
	 * @return file path
	 * @throws IOException 
	 */
	String uploadFile(MultipartFile file) throws IOException;

}
