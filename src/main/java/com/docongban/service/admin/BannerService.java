package com.docongban.service.admin;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface BannerService {
	void uploadBanner(MultipartFile multipartFile, Integer bannerId) throws IOException;
}
