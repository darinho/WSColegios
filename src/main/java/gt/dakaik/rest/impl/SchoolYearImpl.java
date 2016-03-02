package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.GeneralException;
import gt.dakaik.rest.interfaces.WSSchoolYear;
import gt.dakaik.rest.repository.SchoolDayTypeRepository;
import gt.dakaik.rest.repository.SchoolYearRepository;
import gt.entities.SchoolDayType;
import gt.entities.SchoolYear;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SchoolYearImpl implements WSSchoolYear{
    
    Logger eLog = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    SchoolYearRepository repository;
    
    @Autowired
    SchoolDayTypeRepository repoSchoolDayType;
    
    @Override
    public ResponseEntity<SchoolYear> set(int idUsuario, String token, SchoolYear schoolYear) throws GeneralException {
        
        SchoolYear s = repository.findBySchoolDayTypeAndYearNumber(schoolYear.getSchoolDayType(), schoolYear.getYearNumber());
        
        if(s == null){
            /*SchoolDayType schoolDayType = repoSchoolDayType.findOne(schoolYear.getSchoolDayType().getId());
            
            if(schoolDayType != null){
                schoolYear.setSchoolDayType(schoolDayType);
            }*/
            
            repository.save(schoolYear);
        } else {
            return new ResponseEntity("The schoolYear already exists", HttpStatus.ACCEPTED);
        }
        
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<SchoolYear> get(int idUsuario, String token) throws GeneralException {
        
        List<SchoolYear> result = repository.findAll();
        
        return new ResponseEntity((result != null) ? result : new ArrayList<>(), HttpStatus.OK);
    }
    
}