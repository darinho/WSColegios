package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.GeneralException;
import gt.dakaik.rest.interfaces.WSSection;
import gt.dakaik.rest.repository.SectionRepository;
import gt.entities.subject.Section;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component()
public class SectionImpl implements WSSection {
    
    Logger eLog = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    SectionRepository repo;
    
    @Override
    public ResponseEntity<Section> set(String token, Section section) throws GeneralException{
        
        Section s = repo.findByTxtDescription(section.getTxtDescription());
        
        if(s == null){
            repo.save(section);
        } else {
            return new ResponseEntity("The section already exists", HttpStatus.ACCEPTED);
        }
        
        return new ResponseEntity(section, HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<Section> get(String token) throws GeneralException {
        
        List<Section> sections = repo.findAll();
        
        return new ResponseEntity((sections != null) ? sections : new ArrayList<>(), HttpStatus.OK);
    }
    
}