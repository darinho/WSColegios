package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.GeneralException;
import gt.dakaik.rest.interfaces.WSSubjectSection;
import gt.dakaik.rest.repository.SubjectSectionRepository;
import gt.entities.subject.SubjectSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component()
public class SubjectSectionImpl implements WSSubjectSection {
    
    Logger eLog = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    SubjectSectionRepository repo;
    
    @Override
    public ResponseEntity<SubjectSection> set(String token, SubjectSection subjectSection) throws GeneralException {
        
        SubjectSection ss = repo.findBySubjectAndSection(subjectSection.getSubject(), subjectSection.getSection());
        
        if(ss == null){
            repo.save(subjectSection);
        } else {
         return new ResponseEntity("The subject section as already exists", HttpStatus.ACCEPTED);
        }
        
        return new ResponseEntity(subjectSection, HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<SubjectSection> get(String token) throws GeneralException {
        
        return new ResponseEntity(HttpStatus.OK);
    }
    
}