package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.GeneralException;
import gt.dakaik.rest.interfaces.WSSubjectUserProfile;
import gt.dakaik.rest.repository.SubjectUserProfileRepository;
import gt.entities.subject.SubjectUserProfile;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component()
public class SubjectUserProfileImpl implements WSSubjectUserProfile {
    
    @Autowired()
    SubjectUserProfileRepository repository;
    
    @Override
    public ResponseEntity<SubjectUserProfile> set(int idUsuario, String token, SubjectUserProfile subjectUserProfile) throws GeneralException {
        
        SubjectUserProfile s = repository.findBySubjectSectionAndUserProfile(subjectUserProfile.getSubjectSection(), subjectUserProfile.getUserProfile());
        
        if(s == null){
            repository.save(subjectUserProfile);
        } else {
            return new ResponseEntity("Subject user profile already exists", HttpStatus.ACCEPTED);
        }
        
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SubjectUserProfile> get(int idUsuario, String token) throws GeneralException {
        
        List<SubjectUserProfile> result = repository.findAll();
        
        return new ResponseEntity((result != null) ? result : new ArrayList<>(), HttpStatus.OK);
    }
    
}