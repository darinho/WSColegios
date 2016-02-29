/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.Forum;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dario Calderon
 */
@Repository
public interface ForumRepository extends PagingAndSortingRepository<Forum, Long> {

    @Override
    public Forum findOne(Long id);

    @Override
    List<Forum> findAll();

    @Query("SELECT \n"
            + "fu.forum \n"
            + "FROM ForumUser fu \n"
            + "WHERE fu.userProfile.idUserProfile = :userProfile \n"
            + "ORDER BY date DESC")
    Page<Forum> findByUser(@Param("userProfile") long idUserProfile, Pageable pageable);

}
