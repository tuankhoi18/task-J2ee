package com.J2EE23.com.J2EE23.Controller;

import com.J2EE23.com.J2EE23.Entity.Cv;
import com.J2EE23.com.J2EE23.Service.CVService;
import com.J2EE23.com.J2EE23.dto.CVCreationRequest;
import com.J2EE23.com.J2EE23.dto.CVUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/CV")
public class CVController {
    @Autowired
    CVService cvService;
    @GetMapping
    List<Cv> getCV(){
        return cvService.getCV();
    }
    @GetMapping("/{id}")
    Cv getCV(@PathVariable("id") String id){
        return cvService.getCV(id);
    }
    @PostMapping
    Cv createCV(@RequestBody CVCreationRequest request){
        return cvService.createCV(request);
    }
    @PutMapping("/{id}")
    Cv updateCV(@RequestBody CVUpdateRequest request,@PathVariable("id") String id)
    {
        return cvService.updateCV(request,id);
    }
    @DeleteMapping("/{id}")
    String deleteCV(@PathVariable("id") String id){
        cvService.deleteCV(id);
        return "CV has been deleted";
    }
}
