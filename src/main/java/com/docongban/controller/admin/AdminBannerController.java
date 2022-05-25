package com.docongban.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docongban.entity.Banner;
import com.docongban.repository.BannerRepository;

@Controller
@RequestMapping("admin")
public class AdminBannerController {
	
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
	public String handleCreateBanner(@ModelAttribute("banner") Banner banner) {
		
		banner.setUsedStatus("NONE");
		
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
}
