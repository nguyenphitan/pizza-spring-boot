package com.docongban.controller.admin;

import java.io.IOException;

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

import com.docongban.repository.BannerRepository;
import com.docongban.service.admin.BannerService;

@Controller
@RequestMapping("admin")
public class AdminBannerController {
	@Autowired
	BannerService bannerService;

	@Autowired
	BannerRepository bannerRepository;

	@GetMapping("/banner")
	public String getBanner(Model model) {
		model.addAttribute("banners", bannerRepository.findAll());
		return "/admin/banner";
	}

	@GetMapping("/banner/create")
	public String createBanner() {
		return "/admin/formBanner";
	}

	@PostMapping("/banner/save")
	public String handleCreateBanner(
			@RequestParam("image") MultipartFile multipartFile,
			@RequestParam("bannerId") Integer bannerId
	) throws IOException {
		bannerService.uploadBanner(multipartFile, bannerId);
		return "redirect:/admin/banner";
	}

	@GetMapping("/banner/{id}/update")
	public String updateBanner(@PathVariable Integer id, Model model) {
		model.addAttribute("banner", bannerRepository.findById(id).get());
		return "/admin/formBanner";
	}

	@GetMapping("/banner/{id}/delete")
	public String deleteBanner(@PathVariable Integer id, Model model) {
		bannerRepository.delete(bannerRepository.findById(id).get());
		return "redirect:/admin/banner";
	}

	// set used status
	@Transactional
	@GetMapping("/banner/{id}/usedstatus")
	public String changeUsedStatus(@PathVariable int id) {
		bannerRepository.updateUsedStatus(id);
		return "redirect:/admin/banner";
	}

	// set used status
	@Transactional
	@GetMapping("/banner/{id}/nonestatus")
	public String changeNoneStatus(@PathVariable int id) {
		bannerRepository.updateNoneStatus(id);
		return "redirect:/admin/banner";
	}

	// get view usedstatus
	@GetMapping("/banner/view")
	public String getViewBanner(Model model) {
		model.addAttribute("banners", bannerRepository.getViewBanner());
		return "/admin/banner/view";
	}
}
