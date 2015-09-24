package com.animals.app.repository.Impl;

import com.animals.app.domain.UserType;
import com.animals.app.repository.MyBatisConnectionFactory;
import com.animals.app.repository.UserTypeRepository;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by oleg on 24.07.2015.
 */
public class UserTypeRepositoryImpl {

    private SqlSessionFactory sqlSessionFactory;

    public UserTypeRepositoryImpl() {
        sqlSessionFactory = new MyBatisConnectionFactory().getSqlSessionFactory();
    }

    /**
     * Returns a User type instance from the database.
     * @param id primary key value used for lookup.
     * @return A User type instance with a primary key value equals to pk. null if there is no matching row.
     */
    public UserType getById(int id){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserTypeRepository.class).getById(id);
        }
    }

    /**
     * Returns the list of all User types instances from the database.
     * @return the list of all User types instances from the database.
     */
    public List<UserType> getAll(){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserTypeRepository.class).getAll();
        }
    }
}
