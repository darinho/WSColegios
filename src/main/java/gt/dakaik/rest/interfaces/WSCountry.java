/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.interfaces;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.entities.Country;
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
@RequestMapping(value = "/country",
        produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
public interface WSCountry {

    @Transactional(readOnly = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Country> findById(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("id") Long id) throws EntidadNoEncontradaException;

    @Transactional(readOnly = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<Country> findAll(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token
    ) throws EntidadNoEncontradaException;

    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<Country> doCreate(
            @RequestBody Country country, @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token
    ) throws EntidadDuplicadaException;
}
