package gt.dakaik.rest.repository;

import gt.entities.subject.Degree;
import gt.entities.subject.Subject;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface SubjectRepository extends PagingAndSortingRepository<Subject, Long>{
    
    @Override()
    List<Subject> findAll();
    
    Subject findByDegreeAndTxtDescription(Degree degree, String txtDescripcion);
    
}