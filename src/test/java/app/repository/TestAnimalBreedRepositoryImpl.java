package app.repository;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.AnimalBreed;
import com.animals.app.repository.AnimalBreedRepository;
import com.animals.app.repository.Impl.AnimalBreedRepositoryImpl;
import com.animals.app.repository.Impl.AnimalTypeRepositoryImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.naming.NamingException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Rostyslav.Viner on 06.08.2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAnimalBreedRepositoryImpl extends JNDIConfigurationForTests {

    private static AnimalBreedRepository animalBreedRepository;

    private static AnimalBreed actual;

    @BeforeClass
    public static void runBeforeClass() throws Exception {
        configureJNDIForJUnit();

        animalBreedRepository = new AnimalBreedRepositoryImpl();

        actual = new AnimalBreed();
        actual.setBreedUa(RandomStringUtils.random(10, true, true));
        actual.setBreedEn(RandomStringUtils.random(10, true, true));
        actual.setType(new AnimalTypeRepositoryImpl().getAll().get(0));
    }

    @AfterClass
    public static void runAfterClass() {
        animalBreedRepository = null;
        actual = null;
    }

    @Test
    public void test01Insert_ua() throws NamingException {
        assertNotNull(actual);
        assertNull(actual.getId());

        animalBreedRepository.insert_ua(actual);

        assertNotNull(actual.getId());
    }

    //field breedUa is unique
    @Test
    public void test02Insert_ua() {
        assertNotNull(actual);

        animalBreedRepository.insert_ua(actual);
    }

    @Test
    public void test03GetAll() {
        List<AnimalBreed> list = animalBreedRepository.getAll();
        assertNotNull(list);
    }

    @Test
    public void test04GetById() {
        assertNotNull(actual);
        assertNotNull(actual.getId());

        AnimalBreed expected = animalBreedRepository.getById(actual.getId());

        assertNotNull(expected);
    }

    @Test
    public void test05GetById() {
        AnimalBreed expected = animalBreedRepository.getById(-1);

        assertNull(expected);
    }

    @Test
    public void test06GetByTypeId() {
        assertNotNull(actual);
        assertNotNull(actual.getType());
        assertNotNull(actual.getType().getId());

        List<AnimalBreed> expected = animalBreedRepository.getByTypeId(actual.getType().getId());

        assertNotNull(expected);
    }

    @Test
    public void test07GetByTypeId() {
        List<AnimalBreed> expected = animalBreedRepository.getByTypeId(-1);

        assertNotNull(expected);
        assertEquals(expected.size(), 0);
    }

    @Test
    public void test08DeleteById() {
        assertNotNull(actual);
        assertNotNull(actual.getId());

        animalBreedRepository.deleteById(actual.getId());

        AnimalBreed expected = animalBreedRepository.getById(actual.getId());

        assertNull(expected);
    }
}
