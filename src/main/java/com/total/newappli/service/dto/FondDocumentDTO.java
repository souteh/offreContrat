package com.total.newappli.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FondDocument entity.
 */
public class FondDocumentDTO implements Serializable {

    private Long id;

    private String denominationAr;

    @Pattern(regexp = "[A-Z]+")
    private String denominationFr;

    private String formatPj;

    private String reference;

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

    public String getFormatPj() {
        return formatPj;
    }

    public void setFormatPj(String formatPj) {
        this.formatPj = formatPj;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FondDocumentDTO fondDocumentDTO = (FondDocumentDTO) o;
        if (fondDocumentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fondDocumentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FondDocumentDTO{" +
            "id=" + getId() +
            ", denominationAr='" + getDenominationAr() + "'" +
            ", denominationFr='" + getDenominationFr() + "'" +
            ", formatPj='" + getFormatPj() + "'" +
            ", reference='" + getReference() + "'" +
            "}";
    }
}
