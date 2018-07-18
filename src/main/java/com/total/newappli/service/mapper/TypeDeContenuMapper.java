package com.total.newappli.service.mapper;

import com.total.newappli.domain.*;
import com.total.newappli.service.dto.TypeDeContenuDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TypeDeContenu and its DTO TypeDeContenuDTO.
 */
@Mapper(componentModel = "spring", uses = {FondDocumentMapper.class})
public interface TypeDeContenuMapper extends EntityMapper<TypeDeContenuDTO, TypeDeContenu> {

    @Mapping(source = "fondDocument.id", target = "fondDocumentId")
    TypeDeContenuDTO toDto(TypeDeContenu typeDeContenu);

    @Mapping(source = "fondDocumentId", target = "fondDocument")
    TypeDeContenu toEntity(TypeDeContenuDTO typeDeContenuDTO);

    default TypeDeContenu fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeDeContenu typeDeContenu = new TypeDeContenu();
        typeDeContenu.setId(id);
        return typeDeContenu;
    }
}
