package com.docongban.payload;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticalResponse {
	private Date date;
	private Long turnover;
}
