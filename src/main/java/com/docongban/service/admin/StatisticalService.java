package com.docongban.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.docongban.payload.StatisticalResponse;

public interface StatisticalService {
	List<StatisticalResponse> getMonthStatistical(String month, HttpServletRequest request);
}
