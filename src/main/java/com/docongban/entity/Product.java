package com.docongban.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product extends BaseAuditSuperClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private Long price;
	private String thumbnail;

	private String content;
	
	@ManyToOne
	@JoinColumn(name = "id_category")
	private Category category;

	@Transient
	private int catId;
}