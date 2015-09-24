package app.repository;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.Animal;
import com.animals.app.domain.AnimalsFilter;
import com.animals.app.repository.Impl.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Rostyslav.Viner on 24.07.2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAnimalRepositoryImpl extends JNDIConfigurationForTests {
    private static final Logger LOG = LogManager.getLogger(TestAnimalRepositoryImpl.class);

    private static AnimalRepositoryImpl animalRepositoryImpl;
    private static Animal actual;

    @BeforeClass
    public static void runBeforeClass() {
        configureJNDIForJUnit();

        animalRepositoryImpl = new AnimalRepositoryImpl();

        actual = new Animal();
        actual.setSex(Animal.SexType.NONE);
        actual.setType(new AnimalTypeRepositoryImpl().getAll().get(0));
        actual.setSize(Animal.SizeType.NONE);
        actual.setCites(Animal.CitesType.NONE);
        actual.setBreed(new AnimalBreedRepositoryImpl().getAll().get(0));
        actual.setTranspNumber(RandomStringUtils.random(10, true, true));
        actual.setTokenNumber(RandomStringUtils.random(10, true, true));
        actual.setDateOfRegister(new Date(System.currentTimeMillis()));
        actual.setDateOfBirth(new Date(System.currentTimeMillis()));
        actual.setDateOfSterilization(new Date(System.currentTimeMillis()));
        actual.setColor(RandomStringUtils.random(10, true, true));
        actual.setUser(new UserRepositoryImpl().getAll().get(0));
        actual.setAddress(RandomStringUtils.random(10, true, true));
        actual.setActive(true);
        actual.setImage(RandomStringUtils.random(10, true, true));
        actual.setService(new AnimalServiceRepositoryImpl().getAll().get(0));
    }

    @AfterClass
    public static void runAfterClass() {
        actual = null;
        animalRepositoryImpl = null;
    }

    @Test(expected = PersistenceException.class)
     public void test01Insert() {
        Animal test = new Animal();

        assertNull(test.getId());

        animalRepositoryImpl.insert(test);
    }

    @Test(expected = PersistenceException.class)
    public void test02Insert() {
        assertNotNull(actual);
        assertNull(actual.getId());

        actual.setTranspNumber(RandomStringUtils.random(20, true, true)); //max length of transpNumber is 15

        animalRepositoryImpl.insert(actual);
    }

    @Test
    public void test03Insert() {
        assertNotNull(actual);
        assertNull(actual.getId());

        actual.setTranspNumber(RandomStringUtils.random(10, true, true));

        animalRepositoryImpl.insert(actual);

        assertNotNull(actual.getId());
    }

    @Test
    public void test04GetAdminAnimals() {
        List<Animal> expected = animalRepositoryImpl.getAdminAnimals(new AnimalsFilter(1,10));

        assertNotNull(expected);
    }


    @Test
    public void test05GetAdminAnimalsPaginator() {
        AnimalsFilter animalsFilter = new AnimalsFilter(1, 10);
        Animal animal = new Animal();
        animal.setSex(Animal.SexType.FEMALE);
        animal.setSize(Animal.SizeType.SMALL);
        animalsFilter.setAnimal(animal);
        long count = animalRepositoryImpl.getAdminAnimalsPaginator(animalsFilter);

        assertNotEquals(count, 0);
    }

    @Test
    public void test06GetAllForAdopting() {
        List<Animal> expected = animalRepositoryImpl.getAllForAdopting(new AnimalsFilter(1, 10));

        assertNotNull(expected);
    }

    @Test
    public void test07GetById() {
        assertNotNull(actual.getId());

        Animal expected = animalRepositoryImpl.getById(actual.getId());

        assertNotNull(expected);
    }

    @Test
    public void test08GetById() {
        Animal expected = animalRepositoryImpl.getById(-1);

        assertNull(expected);
    }

    @Test
    public void test09Update() {
        actual = animalRepositoryImpl.getById(actual.getId());
        Animal expected = animalRepositoryImpl.getById(actual.getId());

        assertNotNull(actual);
        assertNotNull(expected);
        assertEquals(expected, actual);

        expected.setTranspNumber(RandomStringUtils.random(10, true, true));
        expected.setTokenNumber(RandomStringUtils.random(10, true, true));
        expected.setColor(RandomStringUtils.random(10, true, true));
        expected.setActive(false);
        expected.setImage(RandomStringUtils.random(10, true, true));

        animalRepositoryImpl.update(expected);
        expected = animalRepositoryImpl.getById(expected.getId());

        assertNotNull(expected);
        assertNotSame(expected, actual);
        assertNotEquals(expected, actual);
    }

    @Test(expected = PersistenceException.class)
    public void test10Update() {
        assertNotNull(actual);
        assertNotNull(actual.getId());

        actual.setTranspNumber(RandomStringUtils.random(20, true, true));//max length of transpNumber is 15

        animalRepositoryImpl.update(actual);
    }

    @Test
    public void test11Delete() {
        assertNotNull(actual);
        assertNotNull(actual.getId());

        animalRepositoryImpl.delete(actual.getId());

        Animal expected = animalRepositoryImpl.getById(actual.getId());

        assertNull(expected);
    }
}
