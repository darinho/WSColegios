/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.interfaces;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.entities.School;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dario Calderon
 */
@RestController
@RequestMapping(value = "/school",
        produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
public interface WSSchool {

    @Transactional(readOnly = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<School> findById(
            @RequestParam(value = "idUsuario", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("id") Integer id) throws EntidadNoEncontradaException;

    @Transactional(readOnly = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<School> findAll(
            @RequestParam(value = "idUsuario", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token
    ) throws EntidadNoEncontradaException;

    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<School> doCreate(
            @RequestBody School school, @RequestParam(value = "idUsuario", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token
    ) throws EntidadDuplicadaException, EntidadNoEncontradaException;

    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.PUT)
    public ResponseEntity<School> doUpdate(
            @RequestParam(value = "idUsuario", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @RequestBody School school
    ) throws EntidadNoEncontradaException, EntidadDuplicadaException;

    @Transactional()
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<School> onDelete(
            @RequestParam(value = "idUsuario", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("id") Integer idSchool
    ) throws EntidadNoEncontradaException;
}
