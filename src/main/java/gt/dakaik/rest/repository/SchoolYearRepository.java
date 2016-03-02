package gt.dakaik.rest.repository;

import gt.entities.SchoolDayType;
import gt.entities.SchoolYear;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolYearRepository extends PagingAndSortingRepository<SchoolYear, Long> {
    
    SchoolYear findBySchoolDayTypeAndYearNumber(SchoolDayType schoolDayType, int yearNumber);
    
    @Override()
    List<SchoolYear> findAll();
}