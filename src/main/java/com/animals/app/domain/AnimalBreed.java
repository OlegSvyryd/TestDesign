package com.animals.app.domain;

import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by Rostyslav.Viner on 06.08.2015.
 */
public class AnimalBreed implements Serializable {
    @DecimalMin(value = "1")
    private Long id;

    @Length(max = 45, message = "The breedUa length must be less than {max}.")
    @Pattern(message = "Wrong breedUa.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\-\\s\\(\\)]*")
    private String breedUa;

    @Length(max = 45, message = "The breedUa length must be less than {max}.")
    @Pattern(message = "Wrong breedEn.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\-\\s\\(\\)]*")
    private String breedEn;

    @Valid
    private AnimalType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBreedUa() {
        return breedUa;
    }

    public void setBreedUa(String breedUa) {
        this.breedUa = breedUa;
    }

    public String getBreedEn() {
        return breedEn;
    }

    public void setBreedEn(String breedEn) {
        this.breedEn = breedEn;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimalBreed that = (AnimalBreed) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (breedUa != null ? !breedUa.equals(that.breedUa) : that.breedUa != null) return false;
        if (breedEn != null ? !breedEn.equals(that.breedEn) : that.breedEn != null) return false;
        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (breedUa != null ? breedUa.hashCode() : 0);
        result = 31 * result + (breedEn != null ? breedEn.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnimalBreed{" +
                "id=" + id +
                ", breedUa='" + breedUa + '\'' +
                ", breedEn='" + breedEn + '\'' +
                ", type=" + type +
                '}';
    }
}
