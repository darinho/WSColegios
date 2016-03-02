package gt.dakaik.rest.repository;

import gt.entities.subject.SubjectSection;
import gt.entities.subject.Unit;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface UnitRepository extends PagingAndSortingRepository<Unit, Long>{
    
    @Override()
    List<Unit> findAll();
    
    Unit findBySubjectSectionAndTxtName(SubjectSection subjectSection, String txtName);
    
}