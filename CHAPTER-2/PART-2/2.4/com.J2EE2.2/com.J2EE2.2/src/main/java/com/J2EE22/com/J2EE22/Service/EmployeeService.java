package com.J2EE22.com.J2EE22.Service;

import com.J2EE22.com.J2EE22.Entity.Employee;
import com.J2EE22.com.J2EE22.Repository.EmployeeRepository;
import com.J2EE22.com.J2EE22.dto.EmployeeCreationRequest;
import com.J2EE22.com.J2EE22.dto.EmployeeUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    public Employee createEmployee(EmployeeCreationRequest request){
        Employee employee = new Employee();
        employee.setEmpName(request.getEmpName());
        employee.setPosition(request.getPosition());
        return employeeRepository.save(employee);
    }
    public List<Employee> getEmployee(){
        return employeeRepository.findAll();
    }
    public Employee getEmployee(String id){
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Employee"));
    }
    public Employee updateEmployee(EmployeeUpdateRequest request, String id)
    {
        Employee employee = getEmployee(id);
        employee.setEmpName(request.getEmpName());
        employee.setPosition(request.getPosition());
        return employeeRepository.save(employee);
    }
    public void deleteEmployee(String id)
    {
        employeeRepository.deleteById(id);
    }

}
