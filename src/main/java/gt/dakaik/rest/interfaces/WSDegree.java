package gt.dakaik.rest.interfaces;

import gt.dakaik.exceptions.GeneralException;
import gt.entities.subject.Degree;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Wilver
 */
@RestController()
@RequestMapping(value = "/degree", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
public interface WSDegree {
    
    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<Degree> set(@RequestParam(value = "token", defaultValue = "") String token, @RequestBody(required = true) Degree degree) throws GeneralException;
    
    @Transactional(readOnly = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<Degree> get(@RequestParam(value = "token", defaultValue = "") String token) throws GeneralException;
}
