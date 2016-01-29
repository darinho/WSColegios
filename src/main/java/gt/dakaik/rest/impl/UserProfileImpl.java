/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSUserProfile;
import gt.dakaik.rest.repository.UserProfileRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.entities.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dario Calderon
 */
@Component
public class UserProfileImpl implements WSUserProfile {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository repoU;
    @Autowired
    UserProfileRepository repoUserProfile;

    @Override
    public ResponseEntity<UserProfile> findById(int idUsuario, String token, Long id) throws EntidadNoEncontradaException {
        UserProfile p = repoUserProfile.findOne(id);

        if (p != null) {
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity User Profile");
        }
    }

    @Override
    public ResponseEntity<UserProfile> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoUserProfile.findAll(), HttpStatus.OK);
    }

}
