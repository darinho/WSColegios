/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.Profile;
import gt.entities.School;
import gt.entities.User;
import gt.entities.UserProfile;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dario Calderon
 */
@Repository
public interface UserProfileRepository extends PagingAndSortingRepository<UserProfile, Long> {

    @Override
    public UserProfile findOne(Long id);

    @Override
    List<UserProfile> findAll();

    @Query("select up from UserProfile as up join up.licence as lic "
            + "where lic.school = :school "
            + "and up.user = :user "
            + "and up.profile = :profile "
            + "and up.snActive = true")
    UserProfile findByLicenceYUserYProfile(@Param("school") School school, @Param("user") User user, @Param("profile") Profile profile);

    List<UserProfile> findByUserAndSnActiveTrue(User user);

}
