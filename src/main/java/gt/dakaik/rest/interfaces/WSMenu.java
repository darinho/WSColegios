/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.interfaces;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.entities.Menu;
import gt.entities.ProfileMenu;
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
@RequestMapping(value = "/menu",
        produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
public interface WSMenu {

    @Transactional(readOnly = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Menu> findById(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("id") Long id) throws EntidadNoEncontradaException;

    @Transactional(readOnly = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<Menu> findAll(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token
    ) throws EntidadNoEncontradaException;
    
    @Transactional(readOnly = true)
    @RequestMapping(value = "/{profile}/get", method = RequestMethod.GET)
    public ResponseEntity<Menu> findByUserProfile(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("profile") Long idProfile
    ) throws EntidadNoEncontradaException;

    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<Menu> doCreate(
            @RequestBody Menu menu, @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token
    ) throws EntidadDuplicadaException;

    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.PUT)
    public ResponseEntity<Menu> doUpdate(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @RequestBody Menu menu
    ) throws EntidadNoEncontradaException, EntidadDuplicadaException;

    @Transactional()
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Menu> onDelete(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("id") Long idProfile
    ) throws EntidadNoEncontradaException;
}
