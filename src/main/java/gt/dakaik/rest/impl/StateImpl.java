/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSState;
import gt.dakaik.rest.repository.CityRepository;
import gt.dakaik.rest.repository.SchoolRepository;
import gt.dakaik.rest.repository.StateRepository;
import gt.dakaik.rest.repository.StateRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.entities.City;
import gt.entities.State;
import gt.entities.State;
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
public class StateImpl implements WSState {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository repoU;
    @Autowired
    StateRepository repoState;

    @Override
    public ResponseEntity<State> findById(int idUsuario, String token, Integer id) throws EntidadNoEncontradaException {
        State p = repoState.findOne(id);

        if (p != null) {
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity User");
        }
    }

    @Override
    public ResponseEntity<State> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoState.findAll(), HttpStatus.OK);
    }
}
