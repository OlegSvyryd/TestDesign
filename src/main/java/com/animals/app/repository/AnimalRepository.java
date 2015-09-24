package com.animals.app.repository;

import com.animals.app.domain.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Rostyslav.Viner on 24.07.2015.
 */
public interface AnimalRepository {

    final String INSERT = "INSERT INTO animals (sex, typeId, size, citesType, breed, transpNumber, " +
            "tokenNumber, dateOfRegister, dateOfBirth, dateOfSterilization, color, userId, " +
            "address, isActive, image, serviceId, description) " +
            "VALUES (#{sex}, #{type.id}, #{size}, #{cites}, #{breed.id}, #{transpNumber}, #{tokenNumber}, " +
            "#{dateOfRegister}, #{dateOfBirth}, #{dateOfSterilization}, #{color}, #{user.id}, #{address}, " +
            "#{active}, #{image}, #{service.id}, #{description})";

    final String UPDATE = "UPDATE animals SET sex=#{sex}, typeId=#{type.id}, size=#{size}, " +
            "citesType=#{cites}, breed=#{breed.id}, transpNumber=#{transpNumber}, tokenNumber=#{tokenNumber}, " +
            "dateOfRegister=#{dateOfRegister}, dateOfBirth=#{dateOfBirth}, " +
            "dateOfSterilization=#{dateOfSterilization}, color=#{color}, userId=#{user.id}, " +
            "address=#{address}, isActive=#{active}, image=#{image}, serviceId=#{service.id}, " +
            "description=#{description} " +
            "WHERE id=#{id}";

    final String TWITTER_UPDATE = "UPDATE animals SET dateOfTwitter=#{dateOfTwitter} " +
            "WHERE id=#{id}";

    final String FACEBOOK_UPDATE = "UPDATE animals SET dateOfFacebook=#{dateOfFacebook} " +
            "WHERE id=#{id}";

    final String DELETE = "DELETE FROM animals WHERE id = #{id}";

    final String ADMIN_ANIMALS = "<script>SELECT id, sex, typeId, breed, transpNumber, dateOfBirth, dateOfFacebook, dateOfTwitter, color " +
            "FROM animals " +
            "WHERE id>0 " +
            "<if test = \"animal != null\">" +
                "<if test = \"animal.type != null\"> " +
                    "<if test = \"animal.type.id != null\"> AND typeId=#{animal.type.id} </if> " +
                "</if>" +
                "<if test = \"animal.breed != null\"> " +
                    "<if test = \"animal.breed.id != null\"> AND breed=#{animal.breed.id} </if> " +
                "</if>" +
                "<if test = \"animal.transpNumber != null\"> AND transpNumber=#{animal.transpNumber} </if> " +
                "<if test = \"animal.dateOfRegister != null\"> AND dateOfRegister=#{animal.dateOfRegister} </if> " +
                "<if test = \"animal.sex != null\"> AND sex=#{animal.sex} </if> " +
                "<if test = \"animal.active != null\"> " +
                    "<if test = \"animal.active == true\"> AND isActive=1 </if> " +
                    "<if test = \"animal.active == false\"> AND isActive=0 </if> " +
                "</if>" +
                "<if test = \"animal.service != null\"> " +
                    "<if test = \"animal.service.id != null\"> AND serviceId=#{animal.service.id} </if> " +
                "</if>" +
            "</if> " +
            "LIMIT #{offset},#{limit}</script>";

    final String ADMIN_ANIMALS_PAGINATOR = "<script>SELECT count(*) AS count " +
            "FROM animals " +
            "WHERE id>0 " +
            "<if test = \"animal != null\">" +
                "<if test = \"animal.type != null\"> " +
                    "<if test = \"animal.type.id != null\"> AND typeId=#{animal.type.id} </if> " +
                "</if>" +
                "<if test = \"animal.breed != null\"> " +
                    "<if test = \"animal.breed.id != null\"> AND breed=#{animal.breed.id} </if> " +
                "</if>" +
                "<if test = \"animal.transpNumber != null\"> AND transpNumber=#{animal.transpNumber} </if> " +
                "<if test = \"animal.dateOfRegister != null\"> AND dateOfRegister=#{animal.dateOfRegister} </if> " +
                "<if test = \"animal.sex != null\"> AND sex=#{animal.sex} </if> " +
                "<if test = \"animal.active != null\"> " +
                    "<if test = \"animal.active == true\"> AND isActive=1 </if> " +
                    "<if test = \"animal.active == false\"> AND isActive=0 </if> " +
                "</if>" +
                "<if test = \"animal.service != null\"> " +
                    "<if test = \"animal.service.id != null\"> AND serviceId=#{animal.service.id} </if> " +
                "</if>" +
            "</if> " +
            "</script>";

    final String SELECT_BY_ID = "SELECT id, sex, typeId, size, citesType, breed, transpNumber, tokenNumber, " +
            "dateOfRegister, dateOfBirth, dateOfSterilization, dateOfTwitter, dateOfFacebook, color, userId, address, " +
            "isActive, image, serviceId, description " +
            "FROM animals WHERE id = #{id}";

    final String SELECT_LIST_FOR_ADOPTING = "<script> " +
            "SELECT Id, TypeId, Breed, DateOfBirth, DateOfRegister, image " +
            "FROM animals " +
            "WHERE (serviceId = 1) AND (isActive = 1) " +
            "<if test = \"animal != null\"> " +
            "<if test = \"animal.type != null\"> " +
                "<if test = \"animal.type.id != null\"> AND typeId=#{animal.type.id} </if> " +
            "</if> " +
            "<if test = \"animal.breed != null\"> " +
                "<if test = \"animal.breed.id != null\"> AND breed=#{animal.breed.id} </if> " +
            "</if>" +
            "<if test = \"animal.sex != null\"> AND sex=#{animal.sex} </if> " +
            "<if test = \"animal.dateOfSterilization != null\"> AND dateOfSterilization IS NOT NULL </if> " +
            "<if test = \"animal.size != null\"> AND size=#{animal.size} </if> " +
            "<if test = \"animal.image != null\"> AND image IS NOT NULL </if> " +
            "</if> " +
            "ORDER BY DateOfRegister DESC " +
            "LIMIT #{offset}, #{limit} </script>";

    final String SELECT_LIST_FOUND_ANIMALS = "<script> " +
            "SELECT Id, TypeId, Breed, DateOfBirth, DateOfRegister, image " +
            "FROM animals " +
            "WHERE (serviceId = 2) AND (isActive = 1) " +
            "<if test = \"animal != null\"> " +
            "<if test = \"animal.type != null\"> " +
            "<if test = \"animal.type.id != null\"> AND typeId=#{animal.type.id} </if> " +
            "</if> " +
            "<if test = \"animal.breed != null\"> " +
            "<if test = \"animal.breed.id != null\"> AND breed=#{animal.breed.id} </if> " +
            "</if>" +
            "<if test = \"animal.sex != null\"> AND sex=#{animal.sex} </if> " +
            "<if test = \"animal.dateOfSterilization != null\"> AND dateOfSterilization IS NOT NULL </if> " +
            "<if test = \"animal.size != null\"> AND size=#{animal.size} </if> " +
            "<if test = \"animal.image != null\"> AND image IS NOT NULL </if> " +
            "</if> " +
            "ORDER BY DateOfRegister DESC " +
            "LIMIT #{offset}, #{limit} </script>";

    final String SELECT_LIST_LOST_ANIMALS = "<script> " +
            "SELECT Id, TypeId, Breed, DateOfBirth, DateOfRegister, image " +
            "FROM animals " +
            "WHERE (serviceId = 3) AND (isActive = 1) " +
            "<if test = \"animal != null\"> " +
            "<if test = \"animal.type != null\"> " +
            "<if test = \"animal.type.id != null\"> AND typeId=#{animal.type.id} </if> " +
            "</if> " +
            "<if test = \"animal.breed != null\"> " +
            "<if test = \"animal.breed.id != null\"> AND breed=#{animal.breed.id} </if> " +
            "</if>" +
            "<if test = \"animal.sex != null\"> AND sex=#{animal.sex} </if> " +
            "<if test = \"animal.dateOfSterilization != null\"> AND dateOfSterilization IS NOT NULL </if> " +
            "<if test = \"animal.size != null\"> AND size=#{animal.size} </if> " +
            "<if test = \"animal.image != null\"> AND image IS NOT NULL </if> " +
            "</if> " +
            "ORDER BY DateOfRegister DESC " +
            "LIMIT #{offset}, #{limit} </script>";

    final String SELECT_LIST_FOR_ADOPTING_COUNT = "<script> SELECT count(*) AS count " +
            "FROM animals " +
            "WHERE (isActive = 1) AND (serviceId = 1) " +
            "<if test = \"animal != null\"> " +
            "<if test = \"animal.type != null\"> " +
                "<if test = \"animal.type.id != null\"> AND typeId=#{animal.type.id} </if> " +
            "</if> " +
            "<if test = \"animal.breed != null\"> " +
                "<if test = \"animal.breed.id != null\"> AND breed=#{animal.breed.id} </if> " +
            "</if>" +
            "<if test = \"animal.sex != null\"> AND sex=#{animal.sex} </if> " +
            "<if test = \"animal.dateOfSterilization != null\"> AND dateOfSterilization IS NOT NULL </if> " +
            "<if test = \"animal.size != null\"> AND size=#{animal.size} </if> " +
            "<if test = \"animal.image != null\"> AND image IS NOT NULL </if> " +
            "</if> </script>";

    final String SELECT_LIST_FOUND_ANIMALS_COUNT = "<script> SELECT count(*) AS count " +
            "FROM animals " +
            "WHERE (isActive = 1) AND (serviceId = 2) " +
            "<if test = \"animal != null\"> " +
            "<if test = \"animal.type != null\"> " +
            "<if test = \"animal.type.id != null\"> AND typeId=#{animal.type.id} </if> " +
            "</if> " +
            "<if test = \"animal.breed != null\"> " +
            "<if test = \"animal.breed.id != null\"> AND breed=#{animal.breed.id} </if> " +
            "</if>" +
            "<if test = \"animal.sex != null\"> AND sex=#{animal.sex} </if> " +
            "<if test = \"animal.dateOfSterilization != null\"> AND dateOfSterilization IS NOT NULL </if> " +
            "<if test = \"animal.size != null\"> AND size=#{animal.size} </if> " +
            "<if test = \"animal.image != null\"> AND image IS NOT NULL </if> " +
            "</if> </script>";

    final String SELECT_LIST_LOST_ANIMALS_COUNT = "<script> SELECT count(*) AS count " +
            "FROM animals " +
            "WHERE (isActive = 1) AND (serviceId = 3) " +
            "<if test = \"animal != null\"> " +
            "<if test = \"animal.type != null\"> " +
            "<if test = \"animal.type.id != null\"> AND typeId=#{animal.type.id} </if> " +
            "</if> " +
            "<if test = \"animal.breed != null\"> " +
            "<if test = \"animal.breed.id != null\"> AND breed=#{animal.breed.id} </if> " +
            "</if>" +
            "<if test = \"animal.sex != null\"> AND sex=#{animal.sex} </if> " +
            "<if test = \"animal.dateOfSterilization != null\"> AND dateOfSterilization IS NOT NULL </if> " +
            "<if test = \"animal.size != null\"> AND size=#{animal.size} </if> " +
            "<if test = \"animal.image != null\"> AND image IS NOT NULL </if> " +
            "</if> </script>";


    final String USERPROFILE_SELECT_BY_USER_ID = "SELECT id, sex, typeId, breed, transpNumber, dateOfBirth, color " +
            "FROM animals " +
            "WHERE userId=#{id}";

    final String SELECT_SHORT_INFO_BY_ID = "SELECT id, sex, typeId, citesType, breed, " +
            "dateOfBirth, dateOfSterilization, color, dateOfRegister, size, image " +
            "FROM animals WHERE id = #{id}";   

    final String SELECT_ANIMAL_ID = "SELECT Id " +
            "FROM animals WHERE Id = #{id}";

    /**
     * Insert an instance of Animal into the database.
     * @param animal the instance to be persisted.
     */
    @Insert(INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Animal animal);

    /**
     * Update an instance of Animal in the database.
     * @param animal the instance to be updated.
     */
    @Update(UPDATE)
    void update(Animal animal);

    /**
     * Update date of Twitter publication instance's of Animal in the database.
     * @param animal the instance to be updated.
     */

    @Update(TWITTER_UPDATE)
    void twitterUpdate(Animal animal);

    /**
     * Update date of Facebook publication instance's of Animal in the database.
     * @param animal the instance to be updated.
     */
    @Update(FACEBOOK_UPDATE)
    void facebookUpdate(Animal animal);

    /**
     * Delete an instance of Animal from the database.
     * @param id primary key value of the instance to be deleted.
     */
    @Delete(DELETE)
    void delete(long id);

    /**
     * Returns an Animal instance from the database.
     * @param id primary key value used for lookup.
     * @return An Animal instance with a primary key value equals to pk. null if there is no matching row.
     */

    @Select(SELECT_BY_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="sex", column="sex", javaType = Animal.SexType.class),
            @Result(property="type", column="typeId", javaType = AnimalType.class,
                    one = @One(select = "com.animals.app.repository.AnimalTypeRepository.getById")),
            @Result(property="size", column="size", javaType = Animal.SizeType.class),
            @Result(property="cites", column="citesType", javaType = Animal.CitesType.class),
            @Result(property="breed", column="breed", javaType = AnimalBreed.class,
                    one = @One(select = "com.animals.app.repository.AnimalBreedRepository.getById")),
            @Result(property="transpNumber", column="transpNumber"),
            @Result(property="tokenNumber", column="tokenNumber"),
            @Result(property="dateOfRegister", column="dateOfRegister"),
            @Result(property="dateOfBirth", column="dateOfBirth"),
            @Result(property="dateOfSterilization", column="dateOfSterilization"),
            @Result(property="dateOfTwitter", column="dateOfTwitter"),
            @Result(property="dateOfFacebook", column="dateOfFacebook"),
            @Result(property="color", column="color"),
            @Result(property="user", column="userId", javaType = User.class,
                    one = @One(select = "com.animals.app.repository.UserRepository.getByIdForAdminAnimalList")),
            @Result(property="address", column="address"),
            @Result(property="active", column="isActive"),
            @Result(property="image", column="image"),
            @Result(property="service", column="serviceId", javaType = AnimalService.class,
                    one = @One(select = "com.animals.app.repository.AnimalServiceRepository.getById")),
            @Result(property="description", column="description"),
    })
    Animal getById(long id);

    /**
     * Returns the list of all Animal instances from the database.
     * @return the list of all Animal instances from the database.
     */
    @Select(ADMIN_ANIMALS)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="sex", column="sex", javaType = Animal.SexType.class),
            @Result(property="type", column="typeId", javaType = AnimalType.class,
                    one = @One(select = "com.animals.app.repository.AnimalTypeRepository.getById")),
            @Result(property="breed", column="breed", javaType = AnimalBreed.class,
                    one = @One(select = "com.animals.app.repository.AnimalBreedRepository.getById")),
            @Result(property="transpNumber", column="transpNumber"),
            @Result(property="dateOfBirth", column="dateOfBirth"),
            @Result(property="dateOfFacebook", column="dateOfFacebook"),
            @Result(property="dateOfTwitter", column="dateOfTwitter"),
            @Result(property="color", column="color")
    })
    List<Animal> getAdminAnimals(AnimalsFilter animalsFilter);

    /**
     * Returns count of rows selected from DB by method getAdminAnimalsListByPage
     * @return count of rows selected by getAdminAnimalsListByPage
     */
    @Select(ADMIN_ANIMALS_PAGINATOR)
    long getAdminAnimalsPaginator(AnimalsFilter animalsFilter);

    /**
     * This method return short information about animals for showing on adopting page.
     * @param animalsFilter Separating records for a parts.
     * @return the list of all Animal instances from the database.
     */
    @Select(SELECT_LIST_FOR_ADOPTING)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="type", column="typeId", javaType = AnimalType.class,
                    one = @One(select = "com.animals.app.repository.AnimalTypeRepository.getById")),
            @Result(property="dateOfBirth", column="dateOfBirth"),
            @Result(property="dateOfRegister", column = "dateOfRegister"),
            @Result(property="breed", column="breed", javaType = AnimalBreed.class,
                    one = @One(select = "com.animals.app.repository.AnimalBreedRepository.getById")),
            @Result(property="service", column="serviceId", javaType = AnimalService.class,
                    one = @One(select = "com.animals.app.repository.AnimalServiceRepository.getById")),
            @Result(property="image", column="image"),
    })
    List<Animal> getAllForAdopting(AnimalsFilter animalsFilter);

    /**
     * This method return short information about animals for showing on found page.
     * @param animalsFilter Separating records for a parts.
     * @return the list of all Animal instances from the database.
     */
    @Select(SELECT_LIST_FOUND_ANIMALS)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="type", column="typeId", javaType = AnimalType.class,
                    one = @One(select = "com.animals.app.repository.AnimalTypeRepository.getById")),
            @Result(property="dateOfBirth", column="dateOfBirth"),
            @Result(property="dateOfRegister", column = "dateOfRegister"),
            @Result(property="breed", column="breed", javaType = AnimalBreed.class,
                    one = @One(select = "com.animals.app.repository.AnimalBreedRepository.getById")),
            @Result(property="service", column="serviceId", javaType = AnimalService.class,
                    one = @One(select = "com.animals.app.repository.AnimalServiceRepository.getById")),
            @Result(property="image", column="image"),
    })
    List<Animal> getAllFoundAnimals(AnimalsFilter animalsFilter);

    /**
     * This method return short information about animals for showing on lost page.
     * @param animalsFilter Separating records for a parts.
     * @return the list of all Animal instances from the database.
     */
    @Select(SELECT_LIST_LOST_ANIMALS)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="type", column="typeId", javaType = AnimalType.class,
                    one = @One(select = "com.animals.app.repository.AnimalTypeRepository.getById")),
            @Result(property="dateOfBirth", column="dateOfBirth"),
            @Result(property="dateOfRegister", column = "dateOfRegister"),
            @Result(property="breed", column="breed", javaType = AnimalBreed.class,
                    one = @One(select = "com.animals.app.repository.AnimalBreedRepository.getById")),
            @Result(property="service", column="serviceId", javaType = AnimalService.class,
                    one = @One(select = "com.animals.app.repository.AnimalServiceRepository.getById")),
            @Result(property="image", column="image"),
    })
    List<Animal> getAllLostAnimals(AnimalsFilter animalsFilter);

    /**
     * Returns count of rows selected from DB by method getAllForAdopting
     * @return count of rows selected by getAllForAdopting
     */
    @Select(SELECT_LIST_FOR_ADOPTING_COUNT)
    long getAmountListForAdopting(AnimalsFilter animalsFilter);

    /**
     * Returns count of rows selected from DB by method getAllFoundAnimals
     * @return count of rows selected by getAllFoundAnimals
     */
    @Select(SELECT_LIST_FOUND_ANIMALS_COUNT)
    long getAmountListFoundAnimals(AnimalsFilter animalsFilter);

    /**
     * Returns count of rows selected from DB by method getAllLostAnimals
     * @return count of rows selected by getAllLostAnimals
     */
    @Select(SELECT_LIST_LOST_ANIMALS_COUNT)
    long getAmountListLostAnimals(AnimalsFilter animalsFilter);

    @Select(USERPROFILE_SELECT_BY_USER_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="sex", column="sex", javaType = Animal.SexType.class),
            @Result(property="type", column="typeId", javaType = AnimalType.class,
                    one = @One(select = "com.animals.app.repository.AnimalTypeRepository.getById")),
            @Result(property="breed", column="breed", javaType = AnimalBreed.class,
                    one = @One(select = "com.animals.app.repository.AnimalBreedRepository.getById")),
            @Result(property="transpNumber", column="transpNumber"),
            @Result(property="dateOfBirth", column="dateOfBirth"),
            @Result(property="color", column="color")
    })
    List<Animal> getAnimalByUserId(int parseId);

    /**
     * Returns short information about animal by id.
     * @param id primary key value used for lookup.
     * @return An Animal instance with a primary key value equals to pk. null if there is no matching row.
     */
    @Select(SELECT_SHORT_INFO_BY_ID)
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="sex", column="sex", javaType = Animal.SexType.class),
            @Result(property="type", column="typeId", javaType = AnimalType.class,
                    one = @One(select = "com.animals.app.repository.AnimalTypeRepository.getById")),
            @Result(property="cites", column="citesType", javaType = Animal.CitesType.class),
            @Result(property="breed", column="breed", javaType = AnimalBreed.class,
                    one = @One(select = "com.animals.app.repository.AnimalBreedRepository.getById")),
            @Result(property="dateOfBirth", column="dateOfBirth"),
            @Result(property="dateOfSterilization", column="dateOfSterilization"),
            @Result(property="color", column="color"),
            @Result(property="dateOfRegister", column = "dateOfRegister"),
            @Result(property="size", column="size", javaType = Animal.SizeType.class),
            @Result(property="image", column="image")
	})
    Animal getShortInfoById(long id);


    /**
     * Returns animal id.
     * @param id primary key value used for lookup.
     * @return An Animal id.
     */
    @Select(SELECT_ANIMAL_ID)
    @Results(value = {
            @Result(property="id", column="id"),
    })
    Animal getAnimalId(long id);
}
