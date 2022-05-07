package com.docongban.entity;

import java.time.Instant;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "product")
@Data
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