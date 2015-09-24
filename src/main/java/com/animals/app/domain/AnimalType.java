package com.animals.app.domain;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by Rostyslav.Viner on 22.07.2015.
 */
public class AnimalType implements Serializable{
    @DecimalMin(value = "1")
    private Long id;

    @Length(max = 15, message = "The type length must be less than {max}.")
    @Pattern(message = "Wrong type.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\-\\s]*")
    private String type;

    @Length(max = 15, message = "The typeEn length must be less than {max}.")
    @Pattern(message = "Wrong typeEn.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\-\\s]*")
    private String typeEn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeEn() {
        return typeEn;
    }

    public void setTypeEn(String typeEn) {
        this.typeEn = typeEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimalType that = (AnimalType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return !(typeEn != null ? !typeEn.equals(that.typeEn) : that.typeEn != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (typeEn != null ? typeEn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnimalType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", typeEn='" + typeEn + '\'' +
                '}';
    }
}
