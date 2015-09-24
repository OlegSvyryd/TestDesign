package com.animals.app.repository;

import com.animals.app.domain.AnimalStatus;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 25.08.2015.
 */
public interface AnimalStatusRepository {

    final String SELECT_ALL = "SELECT id, status, statusEn FROM animalstatuses";
    final String SELECT_BY_ID = "SELECT id, status, statusEn FROM animalstatuses WHERE id = #{id}";

    /**
     * Returns the list of all Animal status instances from the database.
     * @return the list of all Animal status instances from the database.
     */
    @Select(SELECT_ALL)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="status", column="status"),
            @Result(property="statusEn", column="statusEn")
    })
    List<AnimalStatus> getAll();

    /**
     * Returns an Animal status instance from the database.
     * @param id primary key value used for lookup.
     * @return An Animal status instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Select(SELECT_BY_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="status", column="status"),
            @Result(property="statusEn", column="statusEn")
    })
    AnimalStatus getById(long id);
}
