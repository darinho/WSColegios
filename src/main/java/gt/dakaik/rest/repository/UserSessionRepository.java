/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.User;
import gt.entities.UserSession;
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
public interface UserSessionRepository extends PagingAndSortingRepository<UserSession, Long> {

    @Query("from UserSession where user = :user and endDate = startDate")
    List<UserSession> getValidUsuarioSesions(@Param("user") User user);

    List<UserSession> findByToken(String txtToken);
}
