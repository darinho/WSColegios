/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.rest.interfaces.WSForum;
import gt.dakaik.rest.repository.ForumRepository;
import gt.dakaik.rest.repository.ForumUserRepository;
import gt.dakaik.rest.repository.UserProfileRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.entities.Forum;
import gt.entities.ForumUser;
import gt.entities.UserProfile;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dario Calderon
 */
@Component
public class ForumImpl implements WSForum {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserProfileRepository repoU;
    @Autowired
    ForumRepository repoForum;
    @Autowired
    ForumUserRepository repoForumUser;

    @Override
    public ResponseEntity<Forum> findById(int idUsuario, String token, Long id) throws EntidadNoEncontradaException {
        Forum p = repoForum.findOne(id);

        if (p != null) {
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity User");
        }
    }

    @Override
    public ResponseEntity<Forum> findByUser(int idUsuario, String token, int page, int idUserProfile) throws EntidadNoEncontradaException {
        Pageable p = new PageRequest(page, 40);

        return new ResponseEntity(repoForum.findByUser(idUserProfile, p), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Forum> onCreate(int idUsuario, String token, char type, long id, Forum forum) throws EntidadNoEncontradaException {

        UserProfile up = repoU.findOne(forum.getUserProfile().getIdUserProfile());

        if (up == null) {
            throw new EntidadNoEncontradaException("Entity UserProfile");
        }

        Forum f = new Forum(forum.getTxtComment(), new Date(), 0, 0, up, forum.getIdForumParent());
        Forum fu;
        List<ForumUser> fusers;
        switch (type) {
            case 'c'://comment
                Forum parent = repoForum.findOne(forum.getIdForumParent());
                if (parent == null) {
                    throw new EntidadNoEncontradaException("Entity Forum");
                }
                parent.setIntComments(parent.getDiscution().size() + 1);
                fu = repoForum.save(f);
                repoForum.save(parent);
                return new ResponseEntity(fu, HttpStatus.OK);
            case 's'://school
                fu = repoForum.save(f);
                fusers = repoForumUser.getBySchool(id);
                fusers.stream().forEach((fus) -> {
                    fus.setForum(fu);
                });
                repoForumUser.save(fusers);
                return new ResponseEntity(fu, HttpStatus.OK);
            case 't'://section
                fu = repoForum.save(f);
                fusers = repoForumUser.getBySectionSubject(id);
                fusers.stream().forEach((fus) -> {
                    fus.setForum(fu);
                });
                repoForumUser.save(fusers);
                return new ResponseEntity(fu, HttpStatus.OK);
            case 'm'://custom Group
                throw new EntidadNoEncontradaException("Entity Forum");
            default:
                throw new EntidadNoEncontradaException("Entity Forum");
        }

    }

}
