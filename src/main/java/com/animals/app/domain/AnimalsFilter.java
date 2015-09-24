package com.animals.app.domain;

import javax.validation.constraints.DecimalMin;

/**
 * Created by Rostyslav.Viner on 11.08.2015.
 */
public class AnimalsFilter {
    @DecimalMin(value = "1")
    private int page;

    @DecimalMin(value = "1")
    private int limit;

    private Animal animal;

    public AnimalsFilter() {
    }

    public AnimalsFilter(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getOffset() {
        return (page-1) * limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public boolean isAnimalFilterNotEmpty(){
        return ((this.page != 0) || (this.limit != 0));
    }

    @Override
    public String toString() {
        return "AnimalsFilter{" +
                "page=" + page +
                ", limit=" + limit +
                ", animal=" + animal +
                '}';
    }
}
