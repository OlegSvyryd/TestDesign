package com.animals.app.repository.Impl;

import com.animals.app.domain.AnimalService;
import com.animals.app.repository.AnimalServiceRepository;
import com.animals.app.repository.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 24.07.2015.
 */
public class AnimalServiceRepositoryImpl implements AnimalServiceRepository {
    private SqlSessionFactory sqlSessionFactory;

    public AnimalServiceRepositoryImpl() {
        sqlSessionFactory = new MyBatisConnectionFactory().getSqlSessionFactory();
    }

    /**
     * Returns the list of all Animal service instances from the database.
     * @return the list of all Animal service instances from the database.
     */
    public List<AnimalService> getAll(){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalServiceRepository.class).getAll();
        }
    }

    /**
     * Returns an Animal service instance from the database.
     * @param id primary key value used for lookup.
     * @return An Animal service instance with a primary key value equals to pk. null if there is no matching row.
     */
    public AnimalService getById(long id){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalServiceRepository.class).getById(id);
        }
    }
}
