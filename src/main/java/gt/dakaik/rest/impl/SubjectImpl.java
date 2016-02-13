package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.GeneralException;
import gt.dakaik.rest.interfaces.WSSubject;
import gt.dakaik.rest.repository.SubjectRepository;
import gt.entities.subject.Subject;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component()
public class SubjectImpl implements WSSubject {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    SubjectRepository repo;
    
    @Override
    public ResponseEntity<Subject> set(String token, Subject subject) throws GeneralException {
        
        Subject s = repo.findByDegreeAndTxtDescription(subject.getDegree(), subject.getTxtDescription());
        
        if(s == null){
            repo.save(subject);
        } else {
            return new ResponseEntity("The subject already exists", HttpStatus.ACCEPTED);
        }
        
        return new ResponseEntity(subject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Subject> get(String token) throws GeneralException {
        
        List<Subject> subjects = repo.findAll();
        
        return new ResponseEntity((subjects != null) ? subjects : new ArrayList<>(),  HttpStatus.OK);
    }

}
