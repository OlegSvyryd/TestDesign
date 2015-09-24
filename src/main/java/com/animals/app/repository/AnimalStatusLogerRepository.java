package com.animals.app.repository;

import com.animals.app.domain.Animal;
import com.animals.app.domain.AnimalStatus;
import com.animals.app.domain.AnimalStatusLoger;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 25.08.2015.
 */
public interface AnimalStatusLogerRepository {

    final String SELECT_BY_ID = "SELECT id, statusId, animalId " +
            "FROM animalstatusesloger WHERE id = #{id}";

    final String GET_ANIMAL_STATUSES = "SELECT Id, animalId, statusId " +
            "FROM animalstatusesloger WHERE animalId = #{animalId}";

    final String SELECT_ALL = "SELECT id, statusId, animalId " +
            "FROM animalstatusesloger";

    /**
     * Returns the list of all Animal status loger instances from the database.
     * @return the list of all Animal status loger instances from the database.
     */
    @Select(SELECT_ALL)
    @Results(value = {
            @Result(property="animal", column="animalId", javaType = Animal.class,
                    one = @One(select = "com.animals.app.repository.AnimalRepository.getAnimalId")),
            @Result(property="animalStatus", column="statusId", javaType = AnimalStatus.class,
                    one = @One(select = "com.animals.app.repository.AnimalStatusRepository.getById")),
    })
    List<AnimalStatusLoger> getAll();

    /**
     * Returns an Animal status loger instance from the database.
     *
     * @param id primary key value used for lookup.
     * @return An Animal status loger instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Select(SELECT_BY_ID)
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property="animal", column="animalId", javaType = Animal.class,
                    one = @One(select = "com.animals.app.repository.AnimalRepository.getById")),
            @Result(property = "date", column = "date")

    })
    AnimalStatusLoger getById(long id);

    @Select(GET_ANIMAL_STATUSES)
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property="animal", column="animalId", javaType = Animal.class,
                    one = @One(select = "com.animals.app.repository.AnimalRepository.getAnimalId")),
            @Result(property="animalStatus", column="statusId", javaType = AnimalStatus.class,
                    one = @One(select = "com.animals.app.repository.AnimalStatusRepository.getById")),
    })
    List<AnimalStatusLoger> getAnimalStatusesByAnimalId(long animalId);

}
