/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.Profile;
import gt.entities.School;
import gt.entities.User;
import gt.entities.Resources;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dario Calderon
 */
@Repository
public interface UserProfileRepository extends PagingAndSortingRepository<Resources, Long> {

    @Override
    public Resources findOne(Long id);

    @Override
    List<Resources> findAll();
    
    Resources findBySchoolAndUserAndProfile(School school, User user, Profile profile);

}
