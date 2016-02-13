package gt.dakaik.rest.repository;

import gt.entities.subject.Section;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface SectionRepository extends PagingAndSortingRepository<Section, Long>{
    
    @Override()
    List<Section> findAll();
    
    Section findByTxtDescription(String txtDescription);
    
}