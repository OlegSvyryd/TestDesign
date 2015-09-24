package com.animals.app.domain;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by Rostyslav.Viner on 22.07.2015.
 */
public class AnimalService implements Serializable{
    @DecimalMin(value = "1")
    private Long id;

    @Length(max = 15, message = "The service length must be less than {max}.")
    @Pattern(message = "Wrong service.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\-\\s]*")
    private String service;

    @Length(max = 15, message = "The service length must be less than {max}.")
    @Pattern(message = "Wrong serviceEn.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\-\\s]*")
    private String serviceEn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceEn() {
        return serviceEn;
    }

    public void setServiceEn(String serviceEn) {
        this.serviceEn = serviceEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimalService that = (AnimalService) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (service != null ? !service.equals(that.service) : that.service != null) return false;
        return !(serviceEn != null ? !serviceEn.equals(that.serviceEn) : that.serviceEn != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (service != null ? service.hashCode() : 0);
        result = 31 * result + (serviceEn != null ? serviceEn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnimalService{" +
                "id=" + id +
                ", service='" + service + '\'' +
                ", serviceEn='" + serviceEn + '\'' +
                '}';
    }
}
