package com.cimb.tokolapak.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cimb.tokolapak.entity.Project;

public interface ProjectRepo extends JpaRepository<Project, Integer>{

}
