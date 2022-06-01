package com.docongban.controller.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.docongban.entity.Banner;
import com.docongban.repository.BannerRepository;

@Controller
@RequestMapping("admin")
public class AdminBannerController {
	private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
	
	@Autowired
	BannerRepository bannerRepository;

	@GetMapping("/banner")
	public String getBanner(Model model) {
		
		List<Banner> banners = bannerRepository.findAll();
		model.addAttribute("banners", banners);
		
		return "/admin/banner"; 
	} 
	
	@GetMapping("/banner/create")
	public String createBanner() {
		return "/admin/formBanner";
	}
	
	@PostMapping("/banner/save")
	public String handleCreateBanner(@RequestParam("image") MultipartFile multipartFile, @RequestParam("bannerId") Integer bannerId) throws IOException {
		
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
		return "redirect:/admin/banner";
	}
	
	@GetMapping("/banner/{id}/update")
	public String updateBanner(@PathVariable Integer id, Model model) {
		
		model.addAttribute("banner", bannerRepository.findById(id).get());
		return "/admin/formBanner";
	}
	
	@GetMapping("/banner/{id}/delete")
	public String deleteBanner(@PathVariable Integer id, Model model) {
		
		Banner banner = bannerRepository.findById(id).get();
		
		bannerRepository.delete(banner);
		return "redirect:/admin/banner";
	}
	
	//set used status
	@Transactional
	@GetMapping("/banner/{id}/usedstatus")
	public String changeUsedStatus(@PathVariable int id) {
		
		bannerRepository.updateUsedStatus(id);
		
		return "redirect:/admin/banner";
	}
	
	//set used status
	@Transactional
	@GetMapping("/banner/{id}/nonestatus")
	public String changeNoneStatus(@PathVariable int id) {
		
		bannerRepository.updateNoneStatus(id);
		
		return "redirect:/admin/banner";
	}
	
	//get view usedstatus
	@GetMapping("/banner/view")
	public String getViewBanner(Model model) {
		
		List<Banner> banners = bannerRepository.getViewBanner();
		model.addAttribute("banners", banners);
		
		return "/admin/banner/view";
	}
	
	
}
