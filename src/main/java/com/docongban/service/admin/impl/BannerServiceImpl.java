package com.docongban.service.admin.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.docongban.entity.Banner;
import com.docongban.repository.BannerRepository;
import com.docongban.service.admin.BannerService;

@Service
public class BannerServiceImpl implements BannerService {
	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
	
	@Autowired
	private BannerRepository bannerRepository;

	@Override
	public void uploadBanner(MultipartFile multipartFile, Integer bannerId) throws IOException {
		Path staticPath = Paths.get("src/main/resources/static");
        Path imagePath = Paths.get("img");
        
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(multipartFile.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
        
        Banner banner = null;
        if( bannerId != -1 ) {
        	banner = bannerRepository.getById(bannerId);
        	banner.setImage(imagePath.resolve(multipartFile.getOriginalFilename()).toString());
        } else {
        	banner = new Banner();
        	banner.setImage(imagePath.resolve(multipartFile.getOriginalFilename()).toString());
        	banner.setUsedStatus("NONE");
        }
        
		
		bannerRepository.save(banner);
	}

}
