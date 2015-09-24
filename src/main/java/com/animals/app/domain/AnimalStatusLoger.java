package com.animals.app.domain;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by oleg on 03.09.2015.
 */
public class AnimalStatusLoger implements Serializable {
    private long id;
    private AnimalStatus animalStatus;
    private Animal animal;
    private User user;
    private Date date;
    private String description;

    public AnimalStatusLoger() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AnimalStatus getAnimalStatus() {
        return animalStatus;
    }

    public void setAnimalStatus(AnimalStatus animalStatus) {
        this.animalStatus = animalStatus;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimalStatusLoger that = (AnimalStatusLoger) o;

        if (id != that.id) return false;
        if (!animal.equals(that.animal)) return false;
        if (!animalStatus.equals(that.animalStatus)) return false;
        if (!date.equals(that.date)) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + animalStatus.hashCode();
        result = 31 * result + animal.hashCode();
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + date.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AnimalStatusLoger{" +
                "id=" + id +
                ", animalStatus=" + animalStatus +
                ", animal=" + animal +
                ", user=" + user +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
