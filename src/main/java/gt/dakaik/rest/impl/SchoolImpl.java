/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSSchool;
import gt.dakaik.rest.repository.CityRepository;
import gt.dakaik.rest.repository.CountryRepository;
import gt.dakaik.rest.repository.SchoolRepository;
import gt.dakaik.rest.repository.StateRepository;
import gt.entities.Address;
import gt.entities.City;
import gt.entities.Country;
import gt.entities.School;
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
public class SchoolImpl implements WSSchool {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SchoolRepository repoSchool;
    @Autowired
    CityRepository repoCity;
    @Autowired
    StateRepository repoState;
    @Autowired
    CountryRepository repoCountry;

    @Override
    public ResponseEntity<School> findById(int idUsuario, String token, Integer id) throws EntidadNoEncontradaException {
        School p = repoSchool.findOne(id);

        if (p != null) {
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity School");
        }
    }

    @Override
    public ResponseEntity<School> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoSchool.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<School> doCreate(School school, int idUsuario, String token) throws EntidadDuplicadaException, EntidadNoEncontradaException {
        School sh = repoSchool.findByNit(school.getNit());

        if (sh != null) {
            throw new EntidadDuplicadaException("Entity School");
        }

        sh = new School();
        sh.setTxtName(school.getTxtName());
        sh.setNit(school.getNit());
        Address ad = new Address();
        ad.setIntZone(school.getAddress().getIntZone());
        ad.setTxtColony(school.getAddress().getTxtColony());
        ad.setTxtIndications(school.getAddress().getTxtIndications());
        ad.setTxtNumberHouse(school.getAddress().getTxtNumberHouse());

        City ct = repoCity.findOne(school.getAddress().getCity().getIdCity());
        if (ct == null) {
            throw new EntidadNoEncontradaException("Entity City");
        }

        ad.setCity(ct);
        sh.setAddress(ad);
        sh.setSnActive(true);
        return new ResponseEntity(repoSchool.save(sh), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<School> doUpdate(int idUsuario, String token, School school) throws EntidadNoEncontradaException, EntidadDuplicadaException {
        School s = repoSchool.findOne(school.getIdSchool());
        if (s == null) {
            throw new EntidadNoEncontradaException("Entity School");
        }

        s.setTxtName(school.getTxtName());

        return new ResponseEntity(repoSchool.save(s), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<School> onDelete(int idUsuario, String token, Integer idSchool) throws EntidadNoEncontradaException {
        School s = repoSchool.findOne(idSchool);
        if (s == null) {
            throw new EntidadNoEncontradaException("Entity School");
        }

        s.setSnActive(Boolean.FALSE);

        return new ResponseEntity(repoSchool.save(s), HttpStatus.OK);
    }

}
