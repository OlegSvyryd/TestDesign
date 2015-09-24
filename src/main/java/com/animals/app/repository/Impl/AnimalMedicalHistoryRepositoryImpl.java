package com.animals.app.repository.Impl;

import com.animals.app.domain.AnimalMedicalHistory;
import com.animals.app.domain.AnimalsFilter;
import com.animals.app.repository.AnimalBreedRepository;
import com.animals.app.repository.AnimalMedicalHistoryRepository;
import com.animals.app.repository.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 25.08.2015.
 */
public class AnimalMedicalHistoryRepositoryImpl implements AnimalMedicalHistoryRepository {

    private SqlSessionFactory sqlSessionFactory;

    public AnimalMedicalHistoryRepositoryImpl() {
        sqlSessionFactory = new MyBatisConnectionFactory().getSqlSessionFactory();
    }

    /**
     * Returns count of rows selected from DB by method getAllForAdopting
     * @return count of rows selected by getAllForAdopting
     */
    public long getByAnimalIdCount(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalMedicalHistoryRepository.class).getByAnimalIdCount(id);
        }
    }

    /**
     * Returns an Animal medical history instance from the database.
     * @param animalId primary key value used for lookup.
     * @return An Animal medical history instance with a primary key value equals to pk. null if there is no matching row.
     */
    public List<AnimalMedicalHistory> getByAnimalId(long animalId, long offset, int limit) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalMedicalHistoryRepository.class).getByAnimalId(animalId, offset, limit);
        }
    }

    /**
     * Returns an Animal medical history instance from the database.
     * @param id primary key value used for lookup.
     * @return An Animal medical history instance with a primary key value equals to pk. null if there is no matching row.
     */
    public AnimalMedicalHistory getById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalMedicalHistoryRepository.class).getById(id);
        }
    }

    /**
     * Delete an instance of Animal medical history from the database.
     * @param id primary key value of the instance to be deleted.
     */
    public void deleteById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(AnimalMedicalHistoryRepository.class).deleteById(id);
            session.commit();
        }
    }

    /**
     * Insert an instance of Animal medical history into the database.
     * @param animalMedicalHistory the instance to be persisted.
     */
    public void insert(AnimalMedicalHistory animalMedicalHistory) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(AnimalMedicalHistoryRepository.class).insert(animalMedicalHistory);
            session.commit();
        }
    }
}
