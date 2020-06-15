package com.cimb.tokolapak.service;

import com.cimb.tokolapak.entity.Department;

public interface DepartmentService {
	public Iterable<Department> getAllDepartments();
	
	public Department addDepartment(Department department);
}
