/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.Country;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dario Calderon
 */
@Repository
public interface CountryRepository extends PagingAndSortingRepository<Country, Long> {

    @Override
    public Country findOne(Long id);

    @Override
    List<Country> findAll();

    @Query("select new gt.entities.Country(c) from Country c")
    List<Country> getAll();

}
