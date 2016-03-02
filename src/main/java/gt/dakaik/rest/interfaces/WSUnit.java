package gt.dakaik.rest.interfaces;

import gt.entities.subject.Unit;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/unit", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public interface WSUnit {
    
    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<Unit> set(
            @RequestParam(value = "idUsuario", defaultValue = "0") int idUsuario,
            @RequestParam(value = "token", defaultValue = "") String token,
            @RequestBody(required = true) Unit unit
    );
    
    @Transactional(readOnly = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<Unit> get(
            @RequestParam(value = "idUsuario", defaultValue = "0") int idUsuario,
            @RequestParam(value = "token", defaultValue = "") String token
    );
    
}