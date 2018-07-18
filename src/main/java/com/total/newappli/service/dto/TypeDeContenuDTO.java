package com.total.newappli.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TypeDeContenu entity.
 */
public class TypeDeContenuDTO implements Serializable {

    private Long id;

    private String denominationAr;

    private String denominationFr;

    @NotNull
    @Size(min = 5, max = 10)
    private String reference;

    @NotNull
    private String code;

    private Long fondDocumentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenominationAr() {
        return denominationAr;
    }

    public void setDenominationAr(String denominationAr) {
        this.denominationAr = denominationAr;
    }

    public String getDenominationFr() {
        return denominationFr;
    }

    public void setDenominationFr(String denominationFr) {
        this.denominationFr = denominationFr;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getFondDocumentId() {
        return fondDocumentId;
    }

    public void setFondDocumentId(Long fondDocumentId) {
        this.fondDocumentId = fondDocumentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TypeDeContenuDTO typeDeContenuDTO = (TypeDeContenuDTO) o;
        if (typeDeContenuDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typeDeContenuDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TypeDeContenuDTO{" +
            "id=" + getId() +
            ", denominationAr='" + getDenominationAr() + "'" +
            ", denominationFr='" + getDenominationFr() + "'" +
            ", reference='" + getReference() + "'" +
            ", code='" + getCode() + "'" +
            ", fondDocument=" + getFondDocumentId() +
            "}";
    }
}
