/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSLicenceType;
import gt.dakaik.rest.repository.LicenceTypeRepository;
import gt.entities.LicenceType;
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
public class LicenceTypeImpl implements WSLicenceType {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    LicenceTypeRepository repoLicenceType;

    @Override
    public ResponseEntity<LicenceType> findById(int idUsuario, String token, Long id) throws EntidadNoEncontradaException {
        LicenceType p = repoLicenceType.findOne(id);

        if (p != null) {
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity LicenceType");
        }
    }

    @Override
    public ResponseEntity<LicenceType> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoLicenceType.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LicenceType> doCreate(LicenceType lictype, int idUsuario, String token) throws EntidadDuplicadaException, EntidadNoEncontradaException {
        LicenceType sh = new LicenceType();
        sh.setTxtDescription(lictype.getTxtDescription());
        sh.setSnActive(true);
        return new ResponseEntity(repoLicenceType.save(sh), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LicenceType> doUpdate(int idUsuario, String token, LicenceType lictype) throws EntidadNoEncontradaException, EntidadDuplicadaException {
        LicenceType s = repoLicenceType.findOne(lictype.getId());
        if (s == null) {
            throw new EntidadNoEncontradaException("Entity LicenceType");
        }

        s.setTxtDescription(lictype.getTxtDescription());

        return new ResponseEntity(repoLicenceType.save(s), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LicenceType> onDelete(int idUsuario, String token, Long idLicenceType) throws EntidadNoEncontradaException {
        LicenceType s = repoLicenceType.findOne(idLicenceType);
        if (s == null) {
            throw new EntidadNoEncontradaException("Entity LicenceType");
        }

        s.setSnActive(Boolean.FALSE);

        return new ResponseEntity(repoLicenceType.save(s), HttpStatus.OK);
    }

}
