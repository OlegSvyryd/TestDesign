package com.animals.app.repository;

import com.animals.app.domain.AnimalService;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 24.07.2015.
 */
public interface AnimalServiceRepository {

    final String SELECT_ALL = "SELECT id, service, serviceEn FROM animalservices";
    final String SELECT_BY_ID = "SELECT id, service, serviceEn FROM animalservices WHERE id = #{id}";

    /**
     * Returns the list of all Animal service instances from the database.
     * @return the list of all Animal service instances from the database.
     */
    @Select(SELECT_ALL)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="service", column="service"),
            @Result(property="serviceEn", column="serviceEn")
    })
    List<AnimalService> getAll();

    /**
     * Returns an Animal service instance from the database.
     * @param id primary key value used for lookup.
     * @return An Animal service instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Select(SELECT_BY_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="service", column="service"),
            @Result(property="serviceEn", column="serviceEn")
    })
    AnimalService getById(long id);
}
