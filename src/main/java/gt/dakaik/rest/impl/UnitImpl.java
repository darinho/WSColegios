package gt.dakaik.rest.impl;

import gt.dakaik.rest.interfaces.WSUnit;
import gt.dakaik.rest.repository.UnitRepository;
import gt.entities.subject.Unit;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component()
public class UnitImpl implements WSUnit {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    UnitRepository repository;
    
    @Override
    public ResponseEntity<Unit> set(int idUsuario, String token, Unit unit) {
        
        Unit u = repository.findBySubjectSectionAndTxtName(unit.getSubjectSection(), unit.getTxtName());
        
        if(u == null){
            repository.save(unit);
        } else {
            return new ResponseEntity("The unit already exists", HttpStatus.ACCEPTED);
        }
        
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Unit> get(int idUsuario, String token) {
        
        List<Unit> units = repository.findAll();
        
        return new ResponseEntity((units != null) ? units : new ArrayList<>(), HttpStatus.OK);
    }
    
    
}