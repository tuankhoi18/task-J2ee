package com.J2EE23.com.J2EE23.Service;

import com.J2EE23.com.J2EE23.Entity.Cv;
import com.J2EE23.com.J2EE23.Repository.CVRepository;
import com.J2EE23.com.J2EE23.dto.CVCreationRequest;
import com.J2EE23.com.J2EE23.dto.CVUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CVService {
    @Autowired
    CVRepository cvRepository;

    public Cv createCV(CVCreationRequest request){
        Cv cv = new Cv();
        cv.setFullName(request.getFullName());
        cv.setCccd(request.getCccd());
        cv.setDateofbirth(request.getDateofbirth());
        cv.setAddress(request.getAddress());
        cv.setEducationLevel(request.getEducationLevel());
        cv.setWorkExperience(request.getWorkExperience());
        return cvRepository.save(cv);
    }
    public List<Cv> getCV(){
        return cvRepository.findAll();
    }
    public Cv getCV(String id){
        return cvRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy CV"));
    }
    public Cv updateCV(CVUpdateRequest request,String id)
    {
        Cv cv = getCV(id);
        cv.setFullName(request.getFullName());
        cv.setCccd(request.getCccd());
        cv.setDateofbirth(request.getDateofbirth());
        cv.setAddress(request.getAddress());
        cv.setEducationLevel(request.getEducationLevel());
        cv.setWorkExperience(request.getWorkExperience());
    return  cvRepository.save(cv);
    }
    public void deleteCV(String id){
        cvRepository.deleteById(id);
    }


}
