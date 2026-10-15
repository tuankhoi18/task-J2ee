package com.J2EE22.com.J2EE22.Controller;

import com.J2EE22.com.J2EE22.Entity.Employee;
import com.J2EE22.com.J2EE22.Service.EmployeeService;
import com.J2EE22.com.J2EE22.dto.EmployeeCreationRequest;
import com.J2EE22.com.J2EE22.dto.EmployeeUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @GetMapping
    List<Employee> getEmployee(){
        return employeeService.getEmployee();
    }
    @GetMapping("/{id}")
    Employee getEmployee(@PathVariable("id") String id){
        return employeeService.getEmployee(id);
    }
    @PostMapping
    Employee createEmployee(@RequestBody EmployeeCreationRequest request){
        return employeeService.createEmployee(request);
    }
    @PutMapping("/{id}")
    Employee updateEmployee(@RequestBody EmployeeUpdateRequest request, @PathVariable("id") String id)
    {
        return employeeService.updateEmployee(request,id);
    }
    @DeleteMapping("/{id}")
    String deleteEmployee(@PathVariable("id") String id)
    {
        employeeService.deleteEmployee(id);
        return "Employee has been deleted";
    }
}
