package app.repository;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.AnimalType;
import com.animals.app.repository.Impl.AnimalTypeRepositoryImpl;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Rostyslav.Viner on 23.07.2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAnimalTypeRepositoryImpl extends JNDIConfigurationForTests {
    private static AnimalTypeRepositoryImpl animalTypeRepositoryImpl;

    @BeforeClass
    public static void runBeforeClass() {
        configureJNDIForJUnit();

        animalTypeRepositoryImpl = new AnimalTypeRepositoryImpl();
    }

    @AfterClass
    public static void runAfterClass() {
        animalTypeRepositoryImpl = null;
    }

    @Test
    public void test01GetAll() {
        List<AnimalType> expected = animalTypeRepositoryImpl.getAll();

        assertNotNull(expected);
        assertNotEquals(expected.size(), 0);
    }

    @Test
    public void test02GetById() {
        long testId = animalTypeRepositoryImpl.getAll().get(0).getId();

        assertNotEquals(testId, 0);

        AnimalType expected = animalTypeRepositoryImpl.getById(testId);

        assertNotNull(expected);
    }

    @Test
    public void test03GetById() {
        AnimalType expected = animalTypeRepositoryImpl.getById(-1);

        assertNull(expected);
    }
}
