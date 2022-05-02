package com.docongban.payload;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalEachOrderAccount {
	private Integer id; 	// order_account_id
	private Long total;
	private Date date;
}
