package com.animals.app.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.animals.app.service.SqlInjection;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Rostyslav.Viner on 22.07.2015.
 */
@XmlRootElement
public class Animal implements Serializable{
    public enum SexType {
        NONE, MALE, FEMALE;
    }

    public enum CitesType {
        NONE, CITES1, CITES2, CITES3
    }

    public enum SizeType {
        NONE, SMALL, MIDDLE, LARGE
    }

    @DecimalMin(value = "1")
    private Long id;

    @NotNull
    private SexType sex;

    @Valid
    @NotNull(message = "Field 'Type' can't be empty")
    private AnimalType type;

    @NotNull(message = "Field 'Size' can't be empty")
    private SizeType size;

    @NotNull(message = "Field 'Cites' can't be empty")
    private CitesType cites;

    @Valid
    @NotNull(message = "Field 'Breed' can't be empty")
    private AnimalBreed breed;

    @Length(max = 15, message = "The transpNumber length must be less than {max}.")
    private String transpNumber;

    @Length(max = 12, message = "The tokenNumber length must be less than {max}.")
    private String tokenNumber;

    @NotNull
    private Date dateOfRegister;

    private Date dateOfBirth;

    private Date dateOfSterilization;

    private Date dateOfFacebook;

    private Date dateOfTwitter;

    @NotNull(message = "The color must be set.")
    @Length(max = 20, message = "The color length must be less than {max}.")
    @Pattern(message = "Wrong color.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\-]*")
    private String color;

    @Length(max = 100, message = "The description length must be less than {max}.")
    @SqlInjection(message = "Wrong description.")
    private String description;

    private User user;

    @NotNull
    @Length(max = 120, message = "The address length must be less than {max}.")
    @Pattern(message = "Wrong address.", regexp = "[A-Za-zА-Яа-яіІїЇ0-9'\\.\\-\\s\\,\\/]*")
    private String address;

    private Boolean active;

    private String image;

    @NotNull
    @Valid
    private AnimalService service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SexType getSex() {
        return sex;
    }

    public void setSex(SexType sex) {
        this.sex = sex;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    public SizeType getSize() {
        return size;
    }

    public void setSize(SizeType size) {
        this.size = size;
    }

    public CitesType getCites() {
        return cites;
    }

    public void setCites(CitesType cites) {
        this.cites = cites;
    }

    public AnimalBreed getBreed() {
        return breed;
    }

    public void setBreed(AnimalBreed breed) {
        this.breed = breed;
    }

    public String getTranspNumber() {
        return transpNumber;
    }

    public void setTranspNumber(String transpNumber) {
        this.transpNumber = transpNumber;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public Date getDateOfRegister() {
        return dateOfRegister;
    }

    public void setDateOfRegister(Date dateOfRegister) {
        this.dateOfRegister = dateOfRegister;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfSterilization() {
        return dateOfSterilization;
    }

    public void setDateOfSterilization(Date dateOfSterilization) {
        this.dateOfSterilization = dateOfSterilization;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public AnimalService getService() {
        return service;
    }

    public void setService(AnimalService service) {
        this.service = service;
    }

    public Date getDateOfFacebook() {
        return dateOfFacebook;
    }

    public void setDateOfFacebook(Date dateOfFacebook) {
        this.dateOfFacebook = dateOfFacebook;
    }

    public Date getDateOfTwitter() {
        return dateOfTwitter;
    }

    public void setDateOfTwitter(Date dateOfTwitter) {
        this.dateOfTwitter = dateOfTwitter;
    }

    public boolean checkNewBreed(AnimalBreed animalBreed){
        return ((animalBreed != null) && (animalBreed.getId() == null) && (animalBreed.getBreedUa() != null));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animal animal = (Animal) o;

        if (id != null ? !id.equals(animal.id) : animal.id != null) return false;
        if (sex != animal.sex) return false;
        if (type != null ? !type.equals(animal.type) : animal.type != null) return false;
        if (size != animal.size) return false;
        if (cites != animal.cites) return false;
        if (breed != null ? !breed.equals(animal.breed) : animal.breed != null) return false;
        if (transpNumber != null ? !transpNumber.equals(animal.transpNumber) : animal.transpNumber != null)
            return false;
        if (tokenNumber != null ? !tokenNumber.equals(animal.tokenNumber) : animal.tokenNumber != null) return false;
        if (dateOfRegister != null ? !dateOfRegister.equals(animal.dateOfRegister) : animal.dateOfRegister != null)
            return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(animal.dateOfBirth) : animal.dateOfBirth != null) return false;
        if (dateOfSterilization != null ? !dateOfSterilization.equals(animal.dateOfSterilization) : animal.dateOfSterilization != null)
            return false;
        if (dateOfFacebook != null ? !dateOfFacebook.equals(animal.dateOfFacebook) : animal.dateOfFacebook != null)
            return false;
        if (dateOfTwitter != null ? !dateOfTwitter.equals(animal.dateOfTwitter) : animal.dateOfTwitter != null)
            return false;
        if (color != null ? !color.equals(animal.color) : animal.color != null) return false;
        if (description != null ? !description.equals(animal.description) : animal.description != null) return false;
        if (user != null ? !user.equals(animal.user) : animal.user != null) return false;
        if (address != null ? !address.equals(animal.address) : animal.address != null) return false;
        if (active != null ? !active.equals(animal.active) : animal.active != null) return false;
        if (image != null ? !image.equals(animal.image) : animal.image != null) return false;
        return !(service != null ? !service.equals(animal.service) : animal.service != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (cites != null ? cites.hashCode() : 0);
        result = 31 * result + (breed != null ? breed.hashCode() : 0);
        result = 31 * result + (transpNumber != null ? transpNumber.hashCode() : 0);
        result = 31 * result + (tokenNumber != null ? tokenNumber.hashCode() : 0);
        result = 31 * result + (dateOfRegister != null ? dateOfRegister.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (dateOfSterilization != null ? dateOfSterilization.hashCode() : 0);
        result = 31 * result + (dateOfFacebook != null ? dateOfFacebook.hashCode() : 0);
        result = 31 * result + (dateOfTwitter != null ? dateOfTwitter.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (service != null ? service.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", sex=" + sex +
                ", type=" + type +
                ", size=" + size +
                ", cites=" + cites +
                ", breed=" + breed +
                ", transpNumber='" + transpNumber + '\'' +
                ", tokenNumber='" + tokenNumber + '\'' +
                ", dateOfRegister=" + dateOfRegister +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfSterilization=" + dateOfSterilization +
                ", dateOfFacebook=" + dateOfFacebook +
                ", dateOfTwitter=" + dateOfTwitter +
                ", color='" + color + '\'' +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", address='" + address + '\'' +
                ", active=" + active +
                ", image='" + image + '\'' +
                ", service=" + service +
                '}';
    }
}
