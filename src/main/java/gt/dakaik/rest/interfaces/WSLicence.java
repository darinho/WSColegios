/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.interfaces;

import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.entities.Licences;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dario Calderon
 */
@RestController
@RequestMapping(value = "/licence",
        produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
public interface WSLicence {

    @Transactional(readOnly = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Licences> findById(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("id") Long id) throws EntidadNoEncontradaException;

    @Transactional(readOnly = true)
    @RequestMapping(value = "/{idSchool}/get", method = RequestMethod.GET)
    public ResponseEntity<Licences> findBySchool(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("idSchool") long idSchool
    ) throws EntidadNoEncontradaException;

    @Transactional()
    @RequestMapping(value = "/{idSchool}/{quantity}/{licType}/set", method = RequestMethod.POST)
    public ResponseEntity<Licences> onCreate(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("idSchool") long idSchool, @PathVariable("quantity") int quantity, @PathVariable("licType") long licType
    ) throws EntidadNoEncontradaException;
}
