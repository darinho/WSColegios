/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.Menu;
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
public interface MenuRepository extends PagingAndSortingRepository<Menu, Long> {

    @Override
    public Menu findOne(Long id);

    @Override
    List<Menu> findAll();
    
    @Query(" SELECT pm.menu from ProfileMenu as pm "
            + "WHERE pm.profile.idProfile = :profile AND pm.snActive = true")
    List<Menu> findByProfile(@Param("profile") Long idProfile);

}
