package com.docongban.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.docongban.entity.Banner;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
	
	@Query(value = "select * from banner where used_status = 'USED' ", nativeQuery = true)
	public List<Banner> getViewBanner();
}
