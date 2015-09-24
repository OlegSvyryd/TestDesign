package com.animals.app.repository;

import com.animals.app.domain.AnimalBreed;
import com.animals.app.domain.AnimalType;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 06.08.2015.
 */

//@CacheNamespace(implementation=org.mybatis.caches.ehcache.EhcacheCache.class)
public interface AnimalBreedRepository {
    final String SELECT_BY_ID = "SELECT id, breedUa, breedEn FROM animalbreeds WHERE id=#{id}";

    final String SELECT_ALL = "SELECT id, breedUa, breedEn, animalTypeId FROM animalbreeds";

    final String SELECT_BY_TYPE_ID = "SELECT id, breedUa, breedEn " +
            "FROM animalbreeds " +
            "WHERE animalTypeId=#{animalTypeId}";

    final String INSERT = "INSERT INTO animalbreeds (breedUa, breedEn, animalTypeId) VALUES (#{breedUa}, #{breedEn}, #{type.id})";

    final String DELETE_BY_ID = "DELETE FROM animalbreeds WHERE id = #{id}";

    /**
     * Returns an AnimalBreed instance from the database.
     * @param id primary key value used for lookup.
     * @return An AnimalBreed instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Select(SELECT_BY_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="breedUa", column="breedUa")
    })
    AnimalBreed getById(long id);

    /**
     * Returns the list of all AnimalBreed instances from the database.
     * @return the list of all AnimalBreed instances from the database.
     */
    @Select(SELECT_ALL)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="breedUa", column="breedUa"),
            @Result(property="breedRu", column="breedRu"),
            @Result(property="breedEn", column="breedEn"),
            @Result(property="type", column="animalTypeId", javaType = AnimalType.class,
                    one = @One(select = "com.animals.app.repository.AnimalTypeRepository.getById"))
    })
    List<AnimalBreed> getAll();

    /**
     * Returns the list of all AnimalBreed instances from the database.
     * @param animalTypeId primary key value used for lookup.
     * @return the list of all AnimalBreed instances from the database.
     */
    @Select(SELECT_BY_TYPE_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="breedUa", column="breedUa")
    })
    List<AnimalBreed> getByTypeId(long animalTypeId);

    /**
     * Insert an instance of AnimalBreed into the database.
     * @param animalBreed the instance to be persisted.
     */
    @Insert(INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id"/*, flushCache = true*/)
    void insert_ua(AnimalBreed animalBreed);

    /**
     * Delete an instance of AnimalBreed from the database.
     * @param id primary key value of the instance to be deleted.
     */
    @Delete(DELETE_BY_ID)
//    @Options(flushCache=true)
    void deleteById(long id);
}
