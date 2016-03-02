package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.GeneralException;
import gt.dakaik.rest.interfaces.WSSchoolDayType;
import gt.dakaik.rest.repository.SchoolDayTypeRepository;
import gt.entities.SchoolDayType;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SchoolDayTypeImpl implements WSSchoolDayType {

    Logger eLog = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SchoolDayTypeRepository repository;

    @Override
    public ResponseEntity<SchoolDayType> set(String token, SchoolDayType schoolDayType) throws GeneralException {

        SchoolDayType s = repository.findByTxtName(schoolDayType.getTxtName());

        if (s == null) {
            repository.save(schoolDayType);
        } else {
            return new ResponseEntity("Entidad duplicada", HttpStatus.ACCEPTED);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SchoolDayType> get(String token) throws GeneralException {
        
        List<SchoolDayType> schoolDayTypes = repository.findAll();
        
        if(schoolDayTypes == null){
            schoolDayTypes = new ArrayList<>();
        }
        
        return new ResponseEntity(schoolDayTypes, HttpStatus.OK);
    }
    
}
