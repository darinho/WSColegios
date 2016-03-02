package gt.dakaik.rest.repository;

import gt.entities.SchoolYear;
import gt.entities.subject.Degree;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface DegreeRepository extends PagingAndSortingRepository<Degree, Long> {
    
    Degree findBySchoolYearAndTxtDescription(SchoolYear schoolYear, String txtDescription);
    
    @Override()
    List<Degree> findAll();
    
}