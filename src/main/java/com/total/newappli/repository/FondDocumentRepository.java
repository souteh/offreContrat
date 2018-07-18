package com.total.newappli.repository;

import com.total.newappli.domain.FondDocument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FondDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FondDocumentRepository extends JpaRepository<FondDocument, Long> {

}
