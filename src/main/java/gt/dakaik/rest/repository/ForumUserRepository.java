/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.Forum;
import gt.entities.ForumUser;
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
public interface ForumUserRepository extends PagingAndSortingRepository<ForumUser, Long> {

    @Override
    public ForumUser findOne(Long id);

    @Override
    List<ForumUser> findAll();

    @Query("SELECT "
            + "new gt.entities.ForumUser(up) \n"
            + "FROM UserProfile as up \n"
            + "JOIN up.licence as lic \n"
            + "JOIN lic.school as s \n"
            + "WHERE s.idSchool = :school")
    List<ForumUser> getBySchool(@Param("school") long idSchool);

    @Query("SELECT "
            + "new gt.entities.ForumUser(sup.userProfile) \n"
            + "FROM SubjectUserProfile as sup \n"
            + "JOIN sup.subjectSection as ss \n"
            + "WHERE ss.id = :subSection \n"
            + "AND sup.snActive= true")
    List<ForumUser> getBySectionSubject(@Param("subSection") long idSubjectSection);
}
