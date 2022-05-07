package com.docongban.service.admin.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.docongban.entity.Account;
import com.docongban.entity.Timekeeping;
import com.docongban.payload.TimekeepingResponse;
import com.docongban.repository.TimekeepingRepository;
import com.docongban.service.admin.SellerService;
import com.docongban.service.admin.TimekeepingService;

/**
 * Xử lý nghiệp vụ chấm công nhân viên
 * Created by: NPTAN (05/05/2022)
 * Version: 1.0
 */
@Service
public class TimekeepingServiceImpl implements TimekeepingService {

	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private TimekeepingRepository timekeepingRepository;
	
	/*
	 * Lấy ra danh sách tất cả các nhân viên
	 * Created by: NPTAN (05/05/2022)
	 * Version: 1.0
	 */
	@Override
	public ModelAndView getAlls() {
		ModelAndView modelAndView = new ModelAndView("admin/timekeeping");
		List<Account> sellers = sellerService.getAllSellers();
		List<TimekeepingResponse> timekeepings = new ArrayList<TimekeepingResponse>();
		
		String pattern = "dd/MM/yyyy";
		DateFormat df = new SimpleDateFormat(pattern);
		String currentDate = df.format(new Date());
		
		// Validate: check xem với account id này, ngày hôm nay, đã có trong bảng chưa?
		for(Account seller : sellers) {
			List<Timekeeping> listTimekeepings = timekeepingRepository.findByAccountId(seller.getId());
			
			if( listTimekeepings != null ) {
				Boolean check = false;
				for(Timekeeping timekeeping : listTimekeepings) {
					String timekeepingDate = df.format(timekeeping.getDate());
					
					// Nếu đã chấm công -> status = true;
					if( timekeepingDate.equals(currentDate) ) {
						timekeepings.add(new TimekeepingResponse(seller, true));
						check = true;
						break;
					}
				}
				if( !check ) {
					timekeepings.add(new TimekeepingResponse(seller, false));
				}
			}
			else { // Nếu trong bảng chấm công chưa có bản ghi nào -> set tất cả các seller là false (chưa chấm công)
				timekeepings.add(new TimekeepingResponse(seller, false));
			}
		}
		
		modelAndView.addObject("timekeepings", timekeepings);
		return modelAndView;
	}

	
	/*
	 * Lưu chấm công
	 * Created by: NPTAN (05/05/2022)
	 * Version: 1.0
	 */
	@Override
	public Timekeeping save(Timekeeping timekeeping) {
		return timekeepingRepository.save(timekeeping);
	}


	/*
	 * Xóa chấm công
	 * Created by: NPTAN (05/05/2022)
	 * Version: 1.0
	 */
	@Override
	public void delete(Integer accountId) {
		timekeepingRepository.deleteByAccountId(accountId);
	}
	
}
