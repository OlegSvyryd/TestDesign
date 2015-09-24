package com.animals.app.repository.Impl;

import com.animals.app.domain.AnimalStatus;
import com.animals.app.repository.AnimalStatusRepository;
import com.animals.app.repository.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 25.08.2015.
 */
public class AnimalStatusRepositoryImpl implements AnimalStatusRepository {

    private SqlSessionFactory sqlSessionFactory;

    public AnimalStatusRepositoryImpl() {
        sqlSessionFactory = new MyBatisConnectionFactory().getSqlSessionFactory();
    }

    /**
     * Returns the list of all Animal status instances from the database.
     * @return the list of all Animal status instances from the database.
     */
    public List<AnimalStatus> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalStatusRepository.class).getAll();
        }
    }

    /**
     * Returns an Animal status instance from the database.
     * @param id primary key value used for lookup.
     * @return An Animal status instance with a primary key value equals to pk. null if there is no matching row.
     */
    public AnimalStatus getById(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalStatusRepository.class).getById(id);
        }
    }
}
