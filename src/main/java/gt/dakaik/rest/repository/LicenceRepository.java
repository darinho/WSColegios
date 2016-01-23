/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.LicenceType;
import gt.entities.Licences;
import gt.entities.School;
import gt.entities.Status;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dario Calderon
 */
@Repository
public interface LicenceRepository extends PagingAndSortingRepository<Licences, Long> {

    @Override
    public Licences findOne(Long id);

    @Override
    List<Licences> findAll();

    Licences findTopBySchoolAndLicenceTypeAndStatusOrderByDatesAsc(School school, LicenceType licenceType, Status status);

    List<Licences> findBySchool(School school);
}
