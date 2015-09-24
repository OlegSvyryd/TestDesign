package com.animals.app.repository;

import com.animals.app.domain.AnimalType;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 23.07.2015.
 */
public interface AnimalTypeRepository {

    final String SELECT_ALL = "SELECT id, type, typeEn FROM animaltypes";
    final String SELECT_BY_ID = "SELECT id, type, typeEn FROM animaltypes WHERE id = #{id}";

    /**
     * Returns the list of all Animal type instances from the database.
     * @return the list of all Animal type instances from the database.
     */
    @Select(SELECT_ALL)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="type", column="type"),
            @Result(property="typeEn", column="typeEn")
})
    List<AnimalType> getAll();

    /**
     * Returns a Animal type instance from the database.
     * @param id primary key value used for lookup.
     * @return A Animal type instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Select(SELECT_BY_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="type", column="type"),
            @Result(property="typeEn", column="typeEn")
    })
    AnimalType getById(long id);
}
