package com.animals.app.repository;

import com.animals.app.domain.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 25.08.2015.
 */
public interface AnimalMedicalHistoryRepository {

    final String SELECT_BY_ANIMAL_ID_PAGINATOR = "SELECT count(*) AS count " +
            "FROM animalstatusesloger WHERE animalId = #{id}";

    final String SELECT_BY_ANIMAL_ID = "SELECT id, statusId, animalId, userId, date, description " +
            "FROM animalstatusesloger WHERE animalId = #{id} LIMIT #{offset},#{limit}";

    final String SELECT_BY_ID = "SELECT id, statusId, animalId, userId, date, description " +
            "FROM animalstatusesloger WHERE id = #{id}";

    final String DELETE_BY_ID = "DELETE " +
            "FROM animalstatusesloger WHERE id = #{id}";

    final String INSERT = "<script>INSERT INTO animalstatusesloger (statusId, animalId, userId, date, description) " +
            "VALUES (#{status.id}, #{animalId}, #{user.id}, " +
            "<choose>" +
                "<when test=\"date == null\">now()</when>" +
                "<otherwise>#{date}</otherwise>" +
            "</choose>, " +
            "#{description})</script>";

    /**
     * Returns count of rows selected from DB by method getAllForAdopting
     * @return count of rows selected by getAllForAdopting
     */
    @Select(SELECT_BY_ANIMAL_ID_PAGINATOR)
    long getByAnimalIdCount(long id);

    /**
     * Returns an Animal medical history instance from the database.
     * @param id primary key value used for lookup.
     * @return An Animal medical history instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Select(SELECT_BY_ANIMAL_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="status", column="statusId", javaType = AnimalStatus.class,
                    one = @One(select = "com.animals.app.repository.AnimalStatusRepository.getById")),
            @Result(property="animalId", column="animalId"),
            @Result(property="user", column="userId", javaType = User.class,
                    one = @One(select = "com.animals.app.repository.UserRepository.getByIdMedicalHistory")),
            @Result(property="date", column="date"),
            @Result(property="description", column="description")
    })
    List<AnimalMedicalHistory> getByAnimalId(@Param("id") long id, @Param("offset") long offset, @Param("limit") int limit);

    /**
     * Returns an Animal medical history instance from the database.
     * @param id primary key value used for lookup.
     * @return An Animal medical history instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Select(SELECT_BY_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="status", column="statusId", javaType = AnimalStatus.class,
                    one = @One(select = "com.animals.app.repository.AnimalStatusRepository.getById")),
            @Result(property="animalId", column="animalId"),
            @Result(property="user", column="userId", javaType = User.class,
                    one = @One(select = "com.animals.app.repository.UserRepository.getByIdMedicalHistory")),
            @Result(property="date", column="date"),
            @Result(property="description", column="description")
    })
    AnimalMedicalHistory getById(long id);

    /**
     * Delete an instance of Animal medical history from the database.
     * @param id primary key value of the instance to be deleted.
     */
    @Delete(DELETE_BY_ID)
    void deleteById(long id);

    /**
     * Insert an instance of Animal medical history into the database.
     * @param animalMedicalHistory the instance to be persisted.
     */
    @Insert(INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AnimalMedicalHistory animalMedicalHistory);
}
