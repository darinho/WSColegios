package gt.dakaik.rest.interfaces;

import gt.dakaik.exceptions.GeneralException;
import gt.entities.subject.SubjectUserProfile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/subjectUserProfile", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public interface WSSubjectUserProfile {
    
    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<SubjectUserProfile> set(
            @RequestParam(value = "idUsuario", defaultValue = "0") int idUsuario,
            @RequestParam(value = "token", defaultValue = "") String token,
            @RequestBody(required = true) SubjectUserProfile subjectUserProfile
    ) throws GeneralException;
    
    @Transactional(readOnly = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<SubjectUserProfile> get(
            @RequestParam(value = "idUsuario", defaultValue = "0") int idUsuario,
            @RequestParam(value = "token", defaultValue = "") String token
    ) throws GeneralException;
    
}