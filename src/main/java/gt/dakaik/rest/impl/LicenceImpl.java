/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.common.Common;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSCity;
import gt.dakaik.rest.interfaces.WSLicence;
import gt.dakaik.rest.repository.CityRepository;
import gt.dakaik.rest.repository.LicenceRepository;
import gt.dakaik.rest.repository.LicenceTypeRepository;
import gt.dakaik.rest.repository.SchoolRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.entities.City;
import gt.entities.LicenceType;
import gt.entities.Licences;
import gt.entities.School;
import gt.entities.Status;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class LicenceImpl implements WSLicence {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository repoU;
    @Autowired
    SchoolRepository repoSchool;
    @Autowired
    LicenceRepository repoLic;
    @Autowired
    LicenceTypeRepository repoLicType;

    @Override
    public ResponseEntity<Licences> findById(int idUsuario, String token, Long id) throws EntidadNoEncontradaException {
        Licences p = repoLic.findOne(id);

        if (p != null) {
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity User");
        }
    }

    @Override
    public ResponseEntity<Licences> findBySchool(int idUsuario, String token, long idSchool) throws EntidadNoEncontradaException {
        School school = repoSchool.findOne(idSchool);

        if (school == null) {
            throw new EntidadNoEncontradaException("Entity School");
        }
        return new ResponseEntity(repoLic.findBySchool(school), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Licences> onCreate(int idUsuario, String token, long idSchool, int quantity, long idLicenceType) throws EntidadNoEncontradaException {
        School school = repoSchool.findOne(idSchool);
        if (school == null) {
            throw new EntidadNoEncontradaException("Entity School");
        }
        LicenceType lt = repoLicType.findOne(idLicenceType);
        if (lt == null) {
            throw new EntidadNoEncontradaException("Entity LicenceType");
        }
        List<Licences> licences = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Licences lic = new Licences();
            lic.setDates(new Date());
            lic.setLicenceType(lt);
            lic.setSchool(school);
            lic.setSnActive(true);
            lic.setStatus(Status.A);
            lic.setTxtLicence(Common.getToken());
            licences.add(lic);
        }
        return new ResponseEntity(repoLic.save(licences), HttpStatus.OK);
    }

}
