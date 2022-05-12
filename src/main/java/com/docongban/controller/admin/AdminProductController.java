package com.docongban.controller.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Category;
import com.docongban.entity.Product;
import com.docongban.repository.CategoryRepository;
import com.docongban.repository.ProductRepository;
import com.docongban.service.ProductService;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
	
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("")
    ModelAndView getAllProducts() {
        ModelAndView modelAndView = new ModelAndView("admin/product");
        List<Product> products = productRepository.findAll();
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/create")
    ModelAndView createProductView() {
        ModelAndView modelAndView = new ModelAndView("admin/formProduct");
        List<Category> categories = categoryRepository.findAll();
        Product newProduct = new Product();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("newProduct", newProduct);
        return modelAndView;
    }

    @PostMapping("/create")
    ModelAndView createProduct(
    		@RequestParam("price") Long price,
    		@RequestParam("title") String title, 
    		@RequestParam("content") String content,
    		@RequestParam("categoryId") Integer categoryId,
    		@RequestParam("thumbnail") MultipartFile multipartFile
    	) throws IOException {
    	
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
        return new ModelAndView("redirect:" + "/admin/products");
    }

    @GetMapping("/{id}/delete")
    ModelAndView delete(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return new ModelAndView("redirect:" + "/admin/products");
    }

    @GetMapping("/{id}/update")
    ModelAndView updateProductView(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/formProduct");
        List<Category> categories = categoryRepository.findAll();
        Product newProduct = new Product();
        Product foundProduct = productRepository.findProductById(id);
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("newProduct", newProduct);
        modelAndView.addObject("updateProduct", foundProduct);
        return modelAndView;
    }

    @PostMapping("/{id}/update")
    ModelAndView updateProduct(@PathVariable Integer id,@ModelAttribute("updateProduct") Product updateProduct) {
        Product toUpdateProduct = productRepository.findProductById(id);
        toUpdateProduct.setTitle(updateProduct.getTitle());
        toUpdateProduct.setThumbnail(updateProduct.getThumbnail());
        toUpdateProduct.setContent(updateProduct.getContent());
        toUpdateProduct.setPrice(updateProduct.getPrice());
        toUpdateProduct.setCategory(updateProduct.getCategory());
        productRepository.save(toUpdateProduct);
        return new ModelAndView("redirect:" + "/admin/products");
    }
}
