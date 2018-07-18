package com.total.newappli.service.impl;

import com.total.newappli.service.TypeDeContenuService;
import com.total.newappli.domain.TypeDeContenu;
import com.total.newappli.repository.TypeDeContenuRepository;
import com.total.newappli.service.dto.TypeDeContenuDTO;
import com.total.newappli.service.mapper.TypeDeContenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing TypeDeContenu.
 */
@Service
@Transactional
public class TypeDeContenuServiceImpl implements TypeDeContenuService {

    private final Logger log = LoggerFactory.getLogger(TypeDeContenuServiceImpl.class);

    private final TypeDeContenuRepository typeDeContenuRepository;

    private final TypeDeContenuMapper typeDeContenuMapper;

    public TypeDeContenuServiceImpl(TypeDeContenuRepository typeDeContenuRepository, TypeDeContenuMapper typeDeContenuMapper) {
        this.typeDeContenuRepository = typeDeContenuRepository;
        this.typeDeContenuMapper = typeDeContenuMapper;
    }

    /**
     * Save a typeDeContenu.
     *
     * @param typeDeContenuDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TypeDeContenuDTO save(TypeDeContenuDTO typeDeContenuDTO) {
        log.debug("Request to save TypeDeContenu : {}", typeDeContenuDTO);
        TypeDeContenu typeDeContenu = typeDeContenuMapper.toEntity(typeDeContenuDTO);
        typeDeContenu = typeDeContenuRepository.save(typeDeContenu);
        return typeDeContenuMapper.toDto(typeDeContenu);
    }

    /**
     * Get all the typeDeContenus.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TypeDeContenuDTO> findAll() {
        log.debug("Request to get all TypeDeContenus");
        return typeDeContenuRepository.findAll().stream()
            .map(typeDeContenuMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one typeDeContenu by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TypeDeContenuDTO> findOne(Long id) {
        log.debug("Request to get TypeDeContenu : {}", id);
        return typeDeContenuRepository.findById(id)
            .map(typeDeContenuMapper::toDto);
    }

    /**
     * Delete the typeDeContenu by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeDeContenu : {}", id);
        typeDeContenuRepository.deleteById(id);
    }
}
