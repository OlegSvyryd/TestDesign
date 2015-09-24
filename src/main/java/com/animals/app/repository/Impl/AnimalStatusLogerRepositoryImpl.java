package com.animals.app.repository.Impl;

import com.animals.app.domain.AnimalStatus;
import com.animals.app.domain.AnimalStatusLoger;
import com.animals.app.repository.AnimalStatusLogerRepository;
import com.animals.app.repository.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 25.08.2015.
 */
public class AnimalStatusLogerRepositoryImpl implements AnimalStatusLogerRepository {

    private SqlSessionFactory sqlSessionFactory;

    public AnimalStatusLogerRepositoryImpl() {
        sqlSessionFactory = new MyBatisConnectionFactory().getSqlSessionFactory();
    }

    /**
     * Returns the list of all Animal status loger instances from the database.
     * @return the list of all Animal status loger instances from the database.
     */
    @Override
    public List<AnimalStatusLoger> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalStatusLogerRepository.class).getAll();
        }
    }

    /**
     * Returns an Animal status loger instance from the database.
     * @param id primary key value used for lookup.
     * @return An Animal status loger instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Override
    public AnimalStatusLoger getById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalStatusLogerRepository.class).getById(id);
        }
    }

    /**
     * Returns List of Animal statuses id from the database.
     *
     * @param animalId primary key value used for lookup.
     * @return List of Animal statuses id.
     */
    @Override
    public List<AnimalStatusLoger> getAnimalStatusesByAnimalId(long animalId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalStatusLogerRepository.class).getAnimalStatusesByAnimalId(animalId);
        }
    }
}
