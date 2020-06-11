package com.cimb.tokolapak.dao;

import org.springframework.data.repository.CrudRepository;

import com.cimb.tokolapak.entity.Employee;

public interface EmployeeRepo extends CrudRepository<Employee, Integer> {

}
