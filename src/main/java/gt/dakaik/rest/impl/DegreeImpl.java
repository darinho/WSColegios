package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.GeneralException;
import gt.dakaik.rest.interfaces.WSDegree;
import gt.dakaik.rest.repository.DegreeRepository;
import gt.entities.subject.Degree;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component()
public class DegreeImpl implements WSDegree {

    @Autowired
    DegreeRepository repository;

    @Override
    public ResponseEntity<Degree> set(String token, Degree degree) throws GeneralException {

        Degree d = repository.findBySchoolYearAndTxtDescription(degree.getSchoolYear(), degree.getTxtDescription());
        
        if(d == null){
            repository.save(degree);
        } else {
            return new ResponseEntity("Degree already exists", HttpStatus.ACCEPTED);
        }
        
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Degree> get(String token) throws GeneralException {
        
        List<Degree> degrees = repository.findAll();
        
        return new ResponseEntity( (degrees != null) ? degrees : new ArrayList<>(), HttpStatus.OK);
    }

}
