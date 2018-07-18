package com.total.newappli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.total.newappli.service.TypeDeContenuService;
import com.total.newappli.web.rest.errors.BadRequestAlertException;
import com.total.newappli.web.rest.util.HeaderUtil;
import com.total.newappli.service.dto.TypeDeContenuDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TypeDeContenu.
 */
@RestController
@RequestMapping("/api")
public class TypeDeContenuResource {

    private final Logger log = LoggerFactory.getLogger(TypeDeContenuResource.class);

    private static final String ENTITY_NAME = "typeDeContenu";

    private final TypeDeContenuService typeDeContenuService;

    public TypeDeContenuResource(TypeDeContenuService typeDeContenuService) {
        this.typeDeContenuService = typeDeContenuService;
    }

    /**
     * POST  /type-de-contenus : Create a new typeDeContenu.
     *
     * @param typeDeContenuDTO the typeDeContenuDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeDeContenuDTO, or with status 400 (Bad Request) if the typeDeContenu has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-de-contenus")
    @Timed
    public ResponseEntity<TypeDeContenuDTO> createTypeDeContenu(@Valid @RequestBody TypeDeContenuDTO typeDeContenuDTO) throws URISyntaxException {
        log.debug("REST request to save TypeDeContenu : {}", typeDeContenuDTO);
        if (typeDeContenuDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeDeContenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeDeContenuDTO result = typeDeContenuService.save(typeDeContenuDTO);
        return ResponseEntity.created(new URI("/api/type-de-contenus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-de-contenus : Updates an existing typeDeContenu.
     *
     * @param typeDeContenuDTO the typeDeContenuDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeDeContenuDTO,
     * or with status 400 (Bad Request) if the typeDeContenuDTO is not valid,
     * or with status 500 (Internal Server Error) if the typeDeContenuDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-de-contenus")
    @Timed
    public ResponseEntity<TypeDeContenuDTO> updateTypeDeContenu(@Valid @RequestBody TypeDeContenuDTO typeDeContenuDTO) throws URISyntaxException {
        log.debug("REST request to update TypeDeContenu : {}", typeDeContenuDTO);
        if (typeDeContenuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeDeContenuDTO result = typeDeContenuService.save(typeDeContenuDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeDeContenuDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-de-contenus : get all the typeDeContenus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeDeContenus in body
     */
    @GetMapping("/type-de-contenus")
    @Timed
    public List<TypeDeContenuDTO> getAllTypeDeContenus() {
        log.debug("REST request to get all TypeDeContenus");
        return typeDeContenuService.findAll();
    }

    /**
     * GET  /type-de-contenus/:id : get the "id" typeDeContenu.
     *
     * @param id the id of the typeDeContenuDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeDeContenuDTO, or with status 404 (Not Found)
     */
    @GetMapping("/type-de-contenus/{id}")
    @Timed
    public ResponseEntity<TypeDeContenuDTO> getTypeDeContenu(@PathVariable Long id) {
        log.debug("REST request to get TypeDeContenu : {}", id);
        Optional<TypeDeContenuDTO> typeDeContenuDTO = typeDeContenuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeDeContenuDTO);
    }

    /**
     * DELETE  /type-de-contenus/:id : delete the "id" typeDeContenu.
     *
     * @param id the id of the typeDeContenuDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-de-contenus/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeDeContenu(@PathVariable Long id) {
        log.debug("REST request to delete TypeDeContenu : {}", id);
        typeDeContenuService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
