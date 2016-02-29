package gt.dakaik.rest.repository;

import gt.entities.subject.Section;
import gt.entities.subject.Subject;
import gt.entities.subject.SubjectSection;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface SubjectSectionRepository extends PagingAndSortingRepository<SubjectSection, Long>{
    
    @Override()
    List<SubjectSection> findAll();
    
    SubjectSection findBySubjectAndSection(Subject subject, Section section);
    
    List<SubjectSection> findBySubject(Subject subject);
    
    List<SubjectSection> findBySection(Section section);
    
}