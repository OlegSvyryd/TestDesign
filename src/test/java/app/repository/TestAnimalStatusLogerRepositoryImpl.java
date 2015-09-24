package app.repository;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.AnimalStatusLoger;
import com.animals.app.repository.Impl.AnimalStatusLogerRepositoryImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by oleg on 14.09.2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAnimalStatusLogerRepositoryImpl extends JNDIConfigurationForTests {

    private static AnimalStatusLogerRepositoryImpl animalStatusLogerRepositoryImpl;

    @BeforeClass
    public static void runBeforeClass() {
        configureJNDIForJUnit();

        animalStatusLogerRepositoryImpl = new AnimalStatusLogerRepositoryImpl();
    }

    @AfterClass
    public static void runAfterClass() {
        animalStatusLogerRepositoryImpl = null;
    }

    @Test
    public void test01GetAll() {
        List<AnimalStatusLoger> expected = animalStatusLogerRepositoryImpl.getAll();

        assertNotNull(expected);
        assertNotEquals(expected.size(), 0);
    }

    @Test
    public void test02GetById() {
        AnimalStatusLoger expected = animalStatusLogerRepositoryImpl.getById(animalStatusLogerRepositoryImpl.getAll().get(0).getId());

        assertNotNull(expected);
    }

    @Test
    public void test03GetStatusByAnimalId(){
        List<AnimalStatusLoger> animalStatuses = animalStatusLogerRepositoryImpl.getAnimalStatusesByAnimalId(animalStatusLogerRepositoryImpl.getAll().get(0).getId());

        assertNotNull(animalStatuses);
    }
}
