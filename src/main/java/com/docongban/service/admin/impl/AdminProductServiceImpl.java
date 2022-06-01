package com.docongban.service.admin.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.docongban.entity.Category;
import com.docongban.entity.Product;
import com.docongban.repository.ProductRepository;
import com.docongban.service.admin.AdminProductService;

@Service
public class AdminProductServiceImpl implements AdminProductService {
	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
	
	@Autowired
	ProductRepository productRepository;

	@Override
	public void create(Long price, String title, String content, Integer categoryId, MultipartFile multipartFile) throws IOException {
		Path staticPath = Paths.get("src/main/resources/static");
        Path imagePath = Paths.get("img");
        // Kiểm tra tồn tại hoặc tạo thư mục /static/images
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(multipartFile.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
    	
    	Category category = new Category();
    	category.setId(categoryId);
    	Product product = new Product();
    	product.setCategory(category);
    	product.setContent(content);
    	product.setPrice(price);
    	product.setTitle(title);
    	product.setThumbnail(imagePath.resolve(multipartFile.getOriginalFilename()).toString());
        productRepository.save(product);
	}

	@Override
	public void update(Integer id, Long price, String title, String content, Integer categoryId, MultipartFile multipartFile) throws IOException {
		Path staticPath = Paths.get("src/main/resources/static");
        Path imagePath = Paths.get("img");
        // Kiểm tra tồn tại hoặc tạo thư mục /static/images
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(multipartFile.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(multipartFile.getBytes());
        }
    	
        Category category = new Category();
    	category.setId(categoryId);
        Product toUpdateProduct = productRepository.findProductById(id);
        toUpdateProduct.setTitle(title);
        toUpdateProduct.setThumbnail(imagePath.resolve(multipartFile.getOriginalFilename()).toString());
        toUpdateProduct.setContent(content);
        toUpdateProduct.setPrice(price);
        toUpdateProduct.setCategory(category);
        productRepository.save(toUpdateProduct);
	}

}
