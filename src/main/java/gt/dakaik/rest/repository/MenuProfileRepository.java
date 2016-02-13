/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.ProfileMenu;
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
public interface MenuProfileRepository extends PagingAndSortingRepository<ProfileMenu, Long> {

    @Override
    public ProfileMenu findOne(Long id);

    @Override
    List<ProfileMenu> findAll();
    
    @Query("select count(*) FROM ProfileMenu as pm join pm.profile as p join pm.menu as m "
            + "WHERE m.idMenu = :menu AND p.idProfile = :profile AND pm.snActive = true")
    Long findByAccessProfile(@Param("menu") Long idMenu, @Param("profile") Long idProfile);

}
