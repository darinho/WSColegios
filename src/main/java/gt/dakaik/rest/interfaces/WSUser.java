/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.interfaces;

import gt.dakaik.dto.DTOSession;
import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.exceptions.GeneralException;
import gt.entities.User;
import gt.entities.UserProfile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Dario Calderon
 */
@RestController
@RequestMapping(value = "/user",
        produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
public interface WSUser {

    @Transactional(readOnly = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> findById(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("id") Long id) throws EntidadNoEncontradaException;

    @Transactional(readOnly = true)
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<User> findAll(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token
    ) throws EntidadNoEncontradaException;

    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<User> doCreate(
            @RequestBody UserProfile pro, @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token
    ) throws EntidadDuplicadaException, EntidadNoEncontradaException;

    @Transactional()
    @RequestMapping(value = "/set", method = RequestMethod.PUT)
    public ResponseEntity<User> doUpdate(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @RequestBody User user
    ) throws EntidadNoEncontradaException, EntidadDuplicadaException;

    @Transactional()
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> onDelete(
            @RequestParam(value = "idUser", defaultValue = "0") int idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @PathVariable("id") Long id
    ) throws EntidadNoEncontradaException;

    @Transactional()
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<DTOSession> login(
            @RequestParam(value = "sUsuario", required = true) String sUsuario,
            @RequestParam(value = "pwd", required = true) String pwd
    ) throws GeneralException;

    @Transactional()
    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public ResponseEntity<DTOSession> getByIdSesion(
            @RequestParam(value = "idUser", defaultValue = "0") Long idUsuario, @RequestParam(value = "token", defaultValue = "") String token,
            @RequestParam(value = "idSession", defaultValue = "0") Long idSession
    ) throws GeneralException;

    @RequestMapping(value = "/{idUser}/uploadimage", method = RequestMethod.POST)
    public ResponseEntity<String> handleFileUpload(@PathVariable("idUser") Long idUser,
            @RequestParam("file") MultipartFile file) throws EntidadNoEncontradaException;

}
