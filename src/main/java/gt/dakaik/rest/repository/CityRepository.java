/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.City;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dario Calderon
 */
@Repository
public interface CityRepository extends PagingAndSortingRepository<City, Integer> {

    @Override
    public City findOne(Integer id);

    @Override
    List<City> findAll();

}
