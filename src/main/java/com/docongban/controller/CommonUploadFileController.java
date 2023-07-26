package com.docongban.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.docongban.service.CommonUploadFileService;

@RestController
@RequestMapping("/api/v1/file")
public class CommonUploadFileController {
	@Autowired
	private CommonUploadFileService commonUploadFileService;
	
	/**
	 * Common upload file.
	 * 
	 * @param file attachment
	 * 
	 * @return file path
	 */
	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		return ResponseEntity.ok(commonUploadFileService.uploadFile(file));
	}

}
