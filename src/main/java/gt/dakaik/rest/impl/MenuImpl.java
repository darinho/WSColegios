/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSMenu;
import gt.dakaik.rest.repository.MenuProfileRepository;
import gt.dakaik.rest.repository.MenuRepository;
import gt.dakaik.rest.repository.ProfileRepository;
import gt.dakaik.rest.repository.SchoolRepository;
import gt.dakaik.rest.repository.UserProfileRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.entities.Menu;
import gt.entities.Profile;
import gt.entities.ProfileMenu;
import gt.entities.User;
import gt.entities.UserProfile;
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
public class MenuImpl implements WSMenu {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository repoU;
    @Autowired
    UserProfileRepository repoUProfile;
    @Autowired
    ProfileRepository repoProfile;
    @Autowired
    SchoolRepository repoSchool;
    @Autowired
    MenuRepository repoMenu;
    @Autowired
    MenuProfileRepository repoProfileMenu;

    @Override
    public ResponseEntity<Menu> findById(int idUsuario, String token, Long id) throws EntidadNoEncontradaException {
        Menu m = repoMenu.findOne(id);

        if (m != null) {
            return new ResponseEntity(m, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity Menu");
        }
    }

    @Override
    public ResponseEntity<Menu> findByUserProfile(int idUsuario, String token, Long idProfile) throws EntidadNoEncontradaException {
        
        List<Menu> menus = repoMenu.findByProfile(idProfile);
        return new ResponseEntity(menus, HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<Menu> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoMenu.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Menu> doCreate(Menu menu, int idUsuario, String token) throws EntidadDuplicadaException {

        Menu m = new Menu(menu.getTxtName(), menu.getTxtLink(), menu.getTxtIcon(), menu.getIdParent(), Boolean.TRUE);
        return new ResponseEntity(repoMenu.save(m), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Menu> doUpdate(int idUsuario, String token, Menu menu) throws EntidadNoEncontradaException, EntidadDuplicadaException {
        Menu m = repoMenu.findOne(menu.getIdMenu());
        if (m == null) {
            throw new EntidadNoEncontradaException("Entity Menu");
        }

        m.setIdParent(menu.getIdParent());
        m.setTxtIcon(menu.getTxtIcon());
        m.setTxtLink(menu.getTxtLink());
        m.setTxtName(menu.getTxtName());

        return new ResponseEntity(repoMenu.save(m), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Menu> onDelete(int idUsuario, String token, Long idMenu) throws EntidadNoEncontradaException {
        Menu m = repoMenu.findOne(idMenu);
        if (m == null) {
            throw new EntidadNoEncontradaException("Entity Menu");
        }

        m.setSnActive(Boolean.FALSE);

        return new ResponseEntity(repoMenu.save(m), HttpStatus.OK);
    }

}
