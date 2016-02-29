package gt.dakaik.rest.repository;

import gt.entities.SchoolDayType;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface SchoolDayTypeRepository extends PagingAndSortingRepository<SchoolDayType, Long>{
    
    @Override()
    List<SchoolDayType> findAll();
    
    SchoolDayType findByTxtName(String txtName);
    
}