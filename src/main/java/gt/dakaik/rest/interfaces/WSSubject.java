package gt.dakaik.rest.interfaces;

import gt.dakaik.exceptions.GeneralException;
import gt.entities.subject.Subject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/subject", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public interface WSSubject{
    
    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<Subject> set(
            @RequestParam(value = "token") String token,
            @RequestBody(required = true) Subject subject
    ) throws GeneralException;
    
    @Transactional(readOnly =true)
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<Subject> get(
            @RequestParam(value = "token") String token
    ) throws GeneralException;
}