package com.docongban.payload;

import javax.validation.constraints.NotBlank;

import com.docongban.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimekeepingResponse {
	@NotBlank
	private Account seller;
	
	@NotBlank
	private Boolean status;
	
}
