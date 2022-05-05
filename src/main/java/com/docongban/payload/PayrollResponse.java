package com.docongban.payload;

import java.util.Date;
import java.util.List;

import com.docongban.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayrollResponse {
	private Account seller;
	private List<Date> dates;
	private Integer totalDate;
}
