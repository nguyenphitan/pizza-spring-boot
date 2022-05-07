package com.docongban.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Timekeeping;
import com.docongban.service.admin.TimekeepingService;

/**
 * Admin chấm công nhân viên
 * Created by: NPTAN (05/05/2022)
 * Version: 1.0
 */
@RestController
@RequestMapping("/admin/timekeeping")
public class AdminTimeKeepingController {
	
	@Autowired
	TimekeepingService timekeepingService;
	
	/*
	 * Hiển thị trang chấm công
	 * Created by: NPTAN (05/05/2022)
	 * Version: 1.0
	 */
	@GetMapping()
	public ModelAndView timekeepingPage() {
		return timekeepingService.getAlls();
	}
	
	
	/*
	 * Chấm công cho nhân viên
	 * Created by: NPTAN (05/05/2022)
	 * Version: 1.0
	 */
	@PostMapping()
	public Timekeeping timekeeping(@RequestBody Timekeeping timekeeping) {
		return timekeepingService.save(timekeeping);
	}
	
	
	/*
	 * Xóa chấm công (sau khi được trả lương)
	 * Created by: NPTAN (05/05/2022)
	 * Version: 1.0
	 */
	@DeleteMapping("/{id}")
	public void deleteTimekeeping(@PathVariable("id") Integer sellerId) {
		timekeepingService.delete(sellerId);
	}
}
