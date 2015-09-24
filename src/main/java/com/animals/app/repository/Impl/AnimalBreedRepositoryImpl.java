package com.animals.app.repository.Impl;

import com.animals.app.domain.AnimalBreed;
import com.animals.app.repository.AnimalBreedRepository;
import com.animals.app.repository.AnimalRepository;
import com.animals.app.repository.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by root on 06.08.2015.
 */
public class AnimalBreedRepositoryImpl implements AnimalBreedRepository {
    private SqlSessionFactory sqlSessionFactory;

    public AnimalBreedRepositoryImpl() {
        sqlSessionFactory = new MyBatisConnectionFactory().getSqlSessionFactory();
    }

    /**
     * Returns an AnimalBreed instance from the database.
     * @param id primary key value used for lookup.
     * @return An AnimalBreed instance with a primary key value equals to pk. null if there is no matching row.
     */
    public AnimalBreed getById(long id){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalBreedRepository.class).getById(id);
        }
    }

    /**
     * Returns the list of all AnimalBreed instances from the database.
     * @return the list of all AnimalBreed instances from the database.
     */
    public List<AnimalBreed> getAll(){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalBreedRepository.class).getAll();
        }
    }

    /**
     * Returns the list of all AnimalBreed instances from the database.
     * @param animalTypeId primary key value used for lookup.
     * @return the list of all AnimalBreed instances from the database.
     */
    public List<AnimalBreed> getByTypeId(long animalTypeId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalBreedRepository.class).getByTypeId(animalTypeId);
        }
    }

    /**
     * @deprecated Not using.
     * Insert an instance of AnimalBreed into the database.
     * @param animalBreed the instance to be persisted.
     */
    @Deprecated
    public void insert_ua(AnimalBreed animalBreed) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(AnimalBreedRepository.class).insert_ua(animalBreed);
            session.commit();
        }
    }

    /**
     * @deprecated Not using.
     * Delete an instance of AnimalBreed from the database.
     * @param id primary key value of the instance to be deleted.
     */
    @Deprecated
    public void deleteById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(AnimalBreedRepository.class).deleteById(id);
            session.commit();
        }
    }
}
