package com.docongban.service.admin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docongban.entity.Account;
import com.docongban.entity.Timekeeping;
import com.docongban.payload.PayrollResponse;
import com.docongban.repository.TimekeepingRepository;
import com.docongban.service.admin.PayrollService;
import com.docongban.service.admin.SellerService;

@Service
public class PayrollServiceImpl implements PayrollService {
	
	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private TimekeepingRepository timekeepingRepository;

	@Override
	public List<PayrollResponse> getAlls() {
		// Danh sách nhân viên + danh sách ngày làm việc:
		List<PayrollResponse> payrolls = new ArrayList<PayrollResponse>();

		// 1. Lấy ra tất cả id của nhân viên:
		List<Account> sellers = sellerService.getAllSellers();

		// 2. Duyệt từng id của nhân viên trong bằng chấm công
		// -> lấy ra các ngày làm việc của nhân viên đó.
		// -> Dữ liệu nhân được sẽ có dạng: (thông tin nhân viên, list các ngày làm
		// việc)
		List<Timekeeping> timekeepings = timekeepingRepository.findAll();

		for (Account seller : sellers) {
			List<Date> dates = new ArrayList<Date>();
			Integer totalDate = 0;
			for (Timekeeping timekeeping : timekeepings) {
				if (seller.getId() == timekeeping.getAccountId()) {
					dates.add(timekeeping.getDate());
					totalDate++;
				}
			}
			payrolls.add(new PayrollResponse(seller, dates, totalDate));
		}
		
		return payrolls;
	}

}
