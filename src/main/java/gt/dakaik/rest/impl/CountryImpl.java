/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSCountry;
import gt.dakaik.rest.repository.CityRepository;
import gt.dakaik.rest.repository.SchoolRepository;
import gt.dakaik.rest.repository.CountryRepository;
import gt.dakaik.rest.repository.StateRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.entities.City;
import gt.entities.Country;
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
public class CountryImpl implements WSCountry {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository repoU;
    @Autowired
    SchoolRepository repoSchool;
    @Autowired
    CountryRepository repoCountry;
    @Autowired
    StateRepository repoState;
    @Autowired
    CityRepository repoCity;

    @Override
    public ResponseEntity<Country> findById(int idUsuario, String token, Long id) throws EntidadNoEncontradaException {
        Country p = repoCountry.findOne(id);

        if (p != null) {
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity User");
        }
    }

    @Override
    public ResponseEntity<Country> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoCountry.getAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Country> doCreate(Country country, int idUsuario, String token) throws EntidadDuplicadaException {
        if (country == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Country c = repoCountry.findOne(country.getId());

        if (c == null) {
            c = new Country();
            c.setTxtName(country.getTxtName());
            repoCountry.save(c);
        }

        for (State st : country.getStates()) {
            State stN = repoState.findOne(st.getId());
            if (stN == null) {
                stN = new State();
                stN.setTxtName(st.getTxtName());
                stN.setCountry(c);
                repoState.save(stN);
            }
            for (City ct : st.getCities()) {
                City ctN = repoCity.findOne(ct.getId());
                if (ctN == null) {
                    ctN = new City();
                    ctN.setTxtName(ct.getTxtName());
                    ctN.setState(stN);
                    repoCity.save(ctN);
                }
            }
        }

        return new ResponseEntity(c, HttpStatus.OK);
    }

}
