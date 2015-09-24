package com.animals.app.repository.Impl;

import com.animals.app.domain.Animal;
import com.animals.app.domain.User;
import com.animals.app.domain.UsersFilter;
import com.animals.app.repository.MyBatisConnectionFactory;
import com.animals.app.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by oleg on 24.07.2015.
 */
public class UserRepositoryImpl implements UserRepository {

    private SqlSessionFactory sqlSessionFactory;

    public UserRepositoryImpl() {
        sqlSessionFactory = new MyBatisConnectionFactory().getSqlSessionFactory();
    }

    /**
     * Insert an instance of User into the database.
     * @param user the instance to be persisted.
     */
    public void insert(User user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(UserRepository.class).insert(user);
            session.commit();
        }
    }

    /**
     * Update an instance of User in the database.
     * @param user the instance to be updated.
     */
    public void update(User user) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(UserRepository.class).update(user);
            session.commit();
        }
    }

    /**
     * Delete an instance of User from the database.
     * @param id primary key value of the instance to be deleted.
     */
    public void delete(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(UserRepository.class).delete(id);
            session.commit();
        }
    }

    /**
     * Returns a User instance from the database.
     * @param id primary key value used for lookup.
     * @return A User instance with a primary key value equals to pk. null if there is no matching row.
     */
    public User getById(int id){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getById(id);
        }
    }

    /**
     * Returns a User instance from the database for admin animals list.
     * @param id primary key value used for lookup.
     * @return A User instance with a primary key value equals to pk. null if there is no matching row.
     */
    public User getByIdForAdminAnimalList(int id){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getByIdForAdminAnimalList(id);
        }
    }

    /**
     * Returns the list of all Users instances from the database.
     * @return the list of all Users instances from the database.
     */
    public List<User> getAll(){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getAll();
        }
    }
    
    /** created 41X
     * Returns a User instance from the database.
     * @param socialLogin and password primary key value used for lookup.
     * @return A User instance with a primary key value equals to pk. null if there is no matching row.
     */
    public User checkIfUserExistInDB(String socialLogin, String password){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).checkIfUserExistInDB(socialLogin, password);
        }
    } 
    
    /** created 41X
     * Returns a String instance from the database.
     * @param socialLogin primary key value used for lookup.
     * @return A String with value equals to pk. null if there is no matching row.
     */
    public String checkIfUsernameUnique(String socialLogin){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).checkIfUsernameUnique(socialLogin);
        }
    }
    
    /** 
     * Returns a User instance from the database.
     * @param googleId value used for lookup.
     * @return A User instance with a GoogleId value equals to pk. null if there is no matching row.
     */
    public User getByGoogleId(String googleId){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getByGoogleId(googleId);
        }
    }
    
    /** 
     * Returns a User instance from the database.
     * @param facebookId value used for lookup.
     * @return A User instance with a FacebookId value equals to pk. null if there is no matching row.
     */
    public User getByFacebookId(String facebookId){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getByFacebookId(facebookId);
        }
    }
    
    /** 
     * Returns a User instance from the database.
     * @param twitterId value used for lookup.
     * @return A User instance with a TwitterId value equals to pk. null if there is no matching row.
     */
    public User getByTwitterId(String twitterId){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getByTwitterId(twitterId);
        }
    }

    /**
     * Returns a User instance from the database for admin animals list.
     * @param id primary key value used for lookup.
     * @return A User instance with a primary key value equals to pk. null if there is no matching row.
     */
    public User getByIdMedicalHistory(long id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getByIdMedicalHistory(id);
        }
    }
    
    /** created 41X
     * Returns a User instance from the database.
     * @param socialLogin and emailVerificator primary key value used for lookup.
     * @return A User instance with a primary key value equals to pk. null if there is no matching row.
     */
    public User userVerification(String socialLogin, String emailVerificationString){
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).userVerification(socialLogin, emailVerificationString);
        }
    }

    /**
     * @return the list of all User instances from the database.
     */
    public List<User> getAdminUsers(UsersFilter usersFilter) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getAdminUsers(usersFilter);
        }
    }

    /**
     * @return count of rows selected by getAdminUsersPaginator
     */
    public long getAdminUsersPaginator(UsersFilter usersFilter) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getAdminUsersPaginator(usersFilter);
        }
    }
    
    /**
     * Returns count of rows selected from DB by method getAnimalByUserIdCount
     *
     * @return count of rows selected by getAnimalByUserIdCount
     */
	public long getAnimalByUserIdCount(long id) throws SqlSessionException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getAnimalByUserIdCount(id);
        }
	}
    
    /**
     * Returns an Animal medical history instance from the database.
     * @param userId primary key value used for lookup.
     * @return An Animal medical history instance with a primary key value equals to pk. null if there is no matching row.
     */
    public List<Animal> getUserAnimals(long userId, long offset, int limit) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).getUserAnimals(userId, offset, limit);
        }
    }

	public User findUserByEmail(String email) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(UserRepository.class).findUserByEmail(email);
        }
	}
}
