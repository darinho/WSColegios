package gt.dakaik.rest.repository;

import gt.entities.UserProfile;
import gt.entities.subject.SubjectSection;
import gt.entities.subject.SubjectUserProfile;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface SubjectUserProfileRepository extends PagingAndSortingRepository<SubjectUserProfile, Long> {
    
    @Override()
    List<SubjectUserProfile> findAll();
    
    SubjectUserProfile findBySubjectSectionAndUserProfile(SubjectSection subjectSection, UserProfile userProfile);
    
}