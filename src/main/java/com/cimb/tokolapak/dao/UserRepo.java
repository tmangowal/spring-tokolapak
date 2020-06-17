package com.cimb.tokolapak.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.tokolapak.entity.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	public Optional<User> findByUsername(String username);
	
}
