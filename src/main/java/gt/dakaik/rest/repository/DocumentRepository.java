/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.repository;

import gt.entities.Document;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dario Calderon
 */
@Repository
public interface DocumentRepository extends PagingAndSortingRepository<Document, Long> {

    @Override
    public Document findOne(Long id);

    @Override
    List<Document> findAll();

    Document findByTxtDocument(String txtDocuent);

}
