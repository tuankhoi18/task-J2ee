package com.J2EE22.com.J2EE22.Repository;

import com.J2EE22.com.J2EE22.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,String> {
    
}
