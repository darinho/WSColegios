/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.School;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dario Calderon
 */
@Repository
public interface SchoolRepository extends PagingAndSortingRepository<School, Long> {

    @Override
    public School findOne(Long id);

    @Override
    List<School> findAll();

    School findByNit(String nit);

}
