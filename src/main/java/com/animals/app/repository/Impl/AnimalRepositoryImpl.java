package com.animals.app.repository.Impl;

import com.animals.app.domain.Animal;
import com.animals.app.domain.AnimalsFilter;
import com.animals.app.repository.AnimalRepository;
import com.animals.app.repository.MyBatisConnectionFactory;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 24.07.2015.
 */
public class AnimalRepositoryImpl implements AnimalRepository {
    private SqlSessionFactory sqlSessionFactory;

    public AnimalRepositoryImpl() {
        sqlSessionFactory = new MyBatisConnectionFactory().getSqlSessionFactory();
    }

    /**
     * Insert an instance of Animal into the database.
     *
     * @param animal the instance to be persisted.
     */
    public void insert(Animal animal) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(AnimalRepository.class).insert(animal);
            session.commit();
        }
    }

    /**
     * Update an instance of Animal in the database.
     *
     * @param animal the instance to be updated.
     */
    public void update(Animal animal) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(AnimalRepository.class).update(animal);
            session.commit();
        }
    }


    /**
     * Update date of Twitter publication instance's of Animal in the database.
     *
     * @param animal the instance to be updated.
     */
    public void twitterUpdate(Animal animal) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(AnimalRepository.class).twitterUpdate(animal);
            session.commit();
        }
    }

    /**
     * Update date of Facebook publication instance's of Animal in the database.
     *
     * @param animal the instance to be updated.
     */
    public void facebookUpdate(Animal animal) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(AnimalRepository.class).facebookUpdate(animal);
            session.commit();
        }
    }

    /**
     * Delete an instance of Animal from the database.
     *
     * @param id primary key value of the instance to be deleted.
     */
    public void delete(long id) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            session.getMapper(AnimalRepository.class).delete(id);
            session.commit();
        }
    }

    /**
     * Returns an Animal instance from the database.
     *
     * @param id primary key value used for lookup.
     * @return An Animal instance with a primary key value equals to pk. null if there is no matching row.
     */
    public Animal getById(long id) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getById(id);
        }
    }

    /**
     * Returns the list of all Animal instances from the database.
     *
     * @return the list of all Animal instances from the database.
     */
    public List<Animal> getAdminAnimals(AnimalsFilter animalsFilter) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAdminAnimals(animalsFilter);
        }
    }

    /**
     * Returns count of rows selected from DB by method getAdminAnimalsListByPage
     *
     * @return count of rows selected by getAdminAnimalsListByPage
     */
    public long getAdminAnimalsPaginator(AnimalsFilter animalsFilter) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAdminAnimalsPaginator(animalsFilter);
        }
    }

    /*
     * This method return short information about animals for showing on adopting page.
     * @param pagenator Separating records for a parts.
     * @return the list of all Animal instances from the database.
     */
    public List<Animal> getAllForAdopting(AnimalsFilter animalsFilter) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAllForAdopting(animalsFilter);
        }
    }

    /*
 * This method return short information about animals for showing on found page.
 * @param pagenator Separating records for a parts.
 * @return the list of all Animal instances from the database.
 */
    public List<Animal> getAllFoundAnimals(AnimalsFilter animalsFilter) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAllFoundAnimals(animalsFilter);
        }
    }

    /*
 * This method return short information about animals for showing on lost page.
 * @param pagenator Separating records for a parts.
 * @return the list of all Animal instances from the database.
 */
    public List<Animal> getAllLostAnimals(AnimalsFilter animalsFilter) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAllLostAnimals(animalsFilter);
        }
    }

    /**
     * Returns count of rows selected from DB by method getAllForAdopting
     *
     * @return count of rows selected by getAllForAdopting
     */
    public long getAmountListForAdopting(AnimalsFilter animalsFilter) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAmountListForAdopting(animalsFilter);
        }
    }

    /**
     * Returns count of rows selected from DB by method getAllFoundAnimals
     *
     * @return count of rows selected by getAllFoundAnimals
     */
    public long getAmountListFoundAnimals(AnimalsFilter animalsFilter) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAmountListFoundAnimals(animalsFilter);
        }
    }

    /**
     * Returns count of rows selected from DB by method getAllLostAnimals
     *
     * @return count of rows selected by getAllLostAnimals
     */
    public long getAmountListLostAnimals(AnimalsFilter animalsFilter) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAmountListLostAnimals(animalsFilter);
        }
    }

    /*
     * This method return short information about animals by UserId for showing on UserCabinet.
     * @return the list of Animal instances from the database with UserId.
     */
    public List<Animal> getAnimalByUserId(int parseId) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAnimalByUserId(parseId);
        }
    }

    /**
     * Returns short information about animal by id.
     *
     * @param id primary key value used for lookup.
     * @return An Animal instance with a primary key value equals to pk. null if there is no matching row.
     */
    public Animal getShortInfoById(long id) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getShortInfoById(id);
        }
    }

    /**
     * Returns animal id.
     * @param id primary key value used for lookup.
     * @return An Animal id.
     */
    @Override
    public Animal getAnimalId(long id) throws PersistenceException{
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return session.getMapper(AnimalRepository.class).getAnimalId(id);
        }
    }
}
