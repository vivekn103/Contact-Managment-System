package com.validation.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.validation.entity.Contacts;

public interface ContactRepository extends JpaRepository<Contacts, Integer>{
	
	@Query("from Contacts as c where c.user.id=:userId")
	public Page<Contacts> findContactsByUser(@Param("userId") int userId, Pageable pagable);
}
