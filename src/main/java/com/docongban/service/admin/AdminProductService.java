package com.docongban.service.admin;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface AdminProductService {
	// create
	void create(Long price, String title, String content, Integer categoryId, MultipartFile multipartFile) throws IOException;
	
	// update
	void update(Integer id, Long price, String title, String content, Integer categoryId, MultipartFile multipartFile) throws IOException;
}
