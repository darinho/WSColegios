/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSProfile;
import gt.dakaik.rest.repository.MenuProfileRepository;
import gt.dakaik.rest.repository.MenuRepository;
import gt.dakaik.rest.repository.ProfileRepository;
import gt.dakaik.rest.repository.SchoolRepository;
import gt.dakaik.rest.repository.UserProfileRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.entities.Menu;
import gt.entities.Profile;
import gt.entities.ProfileMenu;
import java.util.ArrayList;
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
public class ProfileImpl implements WSProfile {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository repoU;
    @Autowired
    UserProfileRepository repoUProfile;
    @Autowired
    MenuProfileRepository repoProfileMenu;
    @Autowired
    MenuRepository repoMenu;
    @Autowired
    SchoolRepository repoSchool;
    @Autowired
    ProfileRepository repoProfile;

    @Override
    public ResponseEntity<Profile> findById(int idUsuario, String token, Long id) throws EntidadNoEncontradaException {
        Profile p = repoProfile.findOne(id);

        if (p != null) {
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity User");
        }
    }

    @Override
    public ResponseEntity<Profile> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoProfile.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Profile> doCreate(Profile profile, int idUsuario, String token) throws EntidadDuplicadaException {
        profile.setSnActive(true);
        return new ResponseEntity(repoProfile.save(profile), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Profile> doUpdate(int idUsuario, String token, Profile profile) throws EntidadNoEncontradaException, EntidadDuplicadaException {
        Profile p = repoProfile.findOne(profile.getIdProfile());
        if (p == null) {
            throw new EntidadNoEncontradaException("Entity User");
        }

        p.setTxtDescription(profile.getTxtDescription());

        return new ResponseEntity(repoProfile.save(p), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Profile> onDelete(int idUsuario, String token, Long idProfile) throws EntidadNoEncontradaException {
        Profile p = repoProfile.findOne(idProfile);
        if (p == null) {
            throw new EntidadNoEncontradaException("Entity User");
        }

        p.setSnActive(Boolean.FALSE);

        return new ResponseEntity(repoProfile.save(p), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> onSetMenus(int idUsuario, String token, Long idProfile, List<Long> idPantallas) throws EntidadNoEncontradaException {

        Profile p = repoProfile.findOne(idProfile);

        if (p == null) {
            throw new EntidadNoEncontradaException("Entity Profile");
        }

        List<ProfileMenu> menus = new ArrayList<>();
        for (Long mid : idPantallas) {
            Menu m = repoMenu.findOne(mid);
            if (m == null) {
                throw new EntidadNoEncontradaException("Entity Menu");
            }
            ProfileMenu pm = repoProfileMenu.findByProfileAndMenu(p, m);
            if (pm == null) {
                pm = new ProfileMenu();
                pm.setMenu(m);
                pm.setProfile(p);
                pm.setSnActive(true);
                menus.add(pm);
            } else if (!pm.isSnActive()) {
                pm.setSnActive(true);
                menus.add(pm);
            }
        }
        repoProfileMenu.save(menus);
        return new ResponseEntity(HttpStatus.OK);
    }

}
