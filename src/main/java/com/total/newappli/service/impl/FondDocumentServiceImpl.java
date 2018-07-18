package com.total.newappli.service.impl;

import com.total.newappli.service.FondDocumentService;
import com.total.newappli.domain.FondDocument;
import com.total.newappli.repository.FondDocumentRepository;
import com.total.newappli.service.dto.FondDocumentDTO;
import com.total.newappli.service.mapper.FondDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing FondDocument.
 */
@Service
@Transactional
public class FondDocumentServiceImpl implements FondDocumentService {

    private final Logger log = LoggerFactory.getLogger(FondDocumentServiceImpl.class);

    private final FondDocumentRepository fondDocumentRepository;

    private final FondDocumentMapper fondDocumentMapper;

    public FondDocumentServiceImpl(FondDocumentRepository fondDocumentRepository, FondDocumentMapper fondDocumentMapper) {
        this.fondDocumentRepository = fondDocumentRepository;
        this.fondDocumentMapper = fondDocumentMapper;
    }

    /**
     * Save a fondDocument.
     *
     * @param fondDocumentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FondDocumentDTO save(FondDocumentDTO fondDocumentDTO) {
        log.debug("Request to save FondDocument : {}", fondDocumentDTO);
        FondDocument fondDocument = fondDocumentMapper.toEntity(fondDocumentDTO);
        fondDocument = fondDocumentRepository.save(fondDocument);
        return fondDocumentMapper.toDto(fondDocument);
    }

    /**
     * Get all the fondDocuments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FondDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FondDocuments");
        return fondDocumentRepository.findAll(pageable)
            .map(fondDocumentMapper::toDto);
    }


    /**
     * Get one fondDocument by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FondDocumentDTO> findOne(Long id) {
        log.debug("Request to get FondDocument : {}", id);
        return fondDocumentRepository.findById(id)
            .map(fondDocumentMapper::toDto);
    }

    /**
     * Delete the fondDocument by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FondDocument : {}", id);
        fondDocumentRepository.deleteById(id);
    }
}
