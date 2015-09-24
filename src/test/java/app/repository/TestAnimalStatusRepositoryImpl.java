package app.repository;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.AnimalStatus;
import com.animals.app.domain.AnimalStatusLoger;
import com.animals.app.repository.Impl.AnimalStatusLogerRepositoryImpl;
import com.animals.app.repository.Impl.AnimalStatusRepositoryImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Rostyslav.Viner on 01.09.2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAnimalStatusRepositoryImpl extends JNDIConfigurationForTests {
    private static AnimalStatusRepositoryImpl animalStatusRepositoryImpl;

    @BeforeClass
    public static void runBeforeClass() {
        configureJNDIForJUnit();

        animalStatusRepositoryImpl = new AnimalStatusRepositoryImpl();
    }

    @AfterClass
    public static void runAfterClass() {
        animalStatusRepositoryImpl = null;
    }

    @Test
    public void test01GetAll() {
        List<AnimalStatus> expected = animalStatusRepositoryImpl.getAll();

        assertNotNull(expected);
        assertNotEquals(expected.size(), 0);
    }

    @Test
    public void test02GetById() {
        long testId = animalStatusRepositoryImpl.getAll().get(0).getId();

        assertNotEquals(testId, 0);

        AnimalStatus expected = animalStatusRepositoryImpl.getById(testId);

        assertNotNull(expected);
    }

    @Test
    public void test03GetById() {
        AnimalStatus expected = animalStatusRepositoryImpl.getById(-1);

        assertNull(expected);
    }
}
