package com.docongban.payload;

import javax.validation.constraints.NotBlank;

import com.docongban.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponse {
	@NotBlank
	private Product product;
	
	@NotBlank
	private Integer quantity;
	
}
