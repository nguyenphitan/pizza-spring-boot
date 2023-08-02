package com.docongban.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.docongban.constants.Constants;
import com.docongban.service.CommonUploadFileService;

@Service
public class CommonUploadFileServiceImpl implements CommonUploadFileService{

	/**
	 * Common upload file.
	 * 
	 * @param file attachment
	 * 
	 * @return file path
	 * @throws IOException 
	 */
	@Override
	public String uploadFile(MultipartFile file) throws IOException {
		Path staticPath = Paths.get("src/main/resources/static");
        Path imagePath = Paths.get("temp");
        if (!Files.exists(Constants.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(Constants.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        
        Path filePath = Constants.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(file.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(filePath)) {
            os.write(file.getBytes());
        }
        
		return filePath.toString();
	}

}
