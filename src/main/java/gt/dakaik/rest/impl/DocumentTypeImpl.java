/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSDocumentType;
import gt.dakaik.rest.repository.DocumentTypeRepository;
import gt.dakaik.rest.repository.SchoolRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.entities.DocumentType;
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
public class DocumentTypeImpl implements WSDocumentType {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository repoU;
    @Autowired
    SchoolRepository repoSchool;
    @Autowired
    DocumentTypeRepository repoDocumentType;

    @Override
    public ResponseEntity<DocumentType> findById(int idUsuario, String token, Long id) throws EntidadNoEncontradaException {
        DocumentType p = repoDocumentType.findOne(id);

        if (p != null) {
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity User");
        }
    }

    @Override
    public ResponseEntity<DocumentType> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoDocumentType.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentType> doCreate(DocumentType documentType, int idUsuario, String token) throws EntidadDuplicadaException {
        return new ResponseEntity(repoDocumentType.save(documentType), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentType> doUpdate(int idUsuario, String token, DocumentType profile) throws EntidadNoEncontradaException, EntidadDuplicadaException {
        throw new EntidadDuplicadaException("Entity User");
    }

    @Override
    public ResponseEntity<DocumentType> onDelete(int idUsuario, String token, Long idDocumentType) throws EntidadNoEncontradaException {
        throw new EntidadNoEncontradaException("Entity User");
    }

}
