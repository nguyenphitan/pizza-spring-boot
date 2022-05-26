package com.docongban.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.docongban.entity.Banner;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
	
	@Query(value = "select * from banner where used_status = 'USED' ", nativeQuery = true)
	public List<Banner> getViewBanner();
	
	@Modifying
	@Query(value = "update banner set used_status = 'USED' where id = ?1 ", nativeQuery = true)
	public void updateUsedStatus(int id);
	
	@Modifying
	@Query(value = "update banner set used_status = 'NONE' where id = ?1 ", nativeQuery = true)
	public void updateNoneStatus(int id);
}
