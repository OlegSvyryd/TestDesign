package com.animals.app.domain;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by Rostyslav.Viner on 25.08.2015.
 */
public class AnimalStatus implements Serializable {
    @DecimalMin(value = "1")
    private Long id;

    @Length(max = 45, message = "The status length must be less than {max}.")
    @Pattern(message = "Wrong status.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\-\\s]*")
    private String status;

    @Length(max = 45, message = "The statusEn length must be less than {max}.")
    @Pattern(message = "Wrong statusEn.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\-\\s]*")
    private String statusEn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusEn() {
        return statusEn;
    }

    public void setStatusEn(String statusEn) {
        this.statusEn = statusEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimalStatus that = (AnimalStatus) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return !(statusEn != null ? !statusEn.equals(that.statusEn) : that.statusEn != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (statusEn != null ? statusEn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnimalStatus{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", statusEn='" + statusEn + '\'' +
                '}';
    }
}
