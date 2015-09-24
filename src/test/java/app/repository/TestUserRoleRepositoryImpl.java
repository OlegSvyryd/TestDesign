package app.repository;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.UserRole;
import com.animals.app.repository.Impl.UserRoleRepositoryImpl;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by oleg on 24.07.2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserRoleRepositoryImpl extends JNDIConfigurationForTests{

    private static UserRoleRepositoryImpl userRoleRepository;

    @BeforeClass
    public static void runBeforeClass() {
        configureJNDIForJUnit();
        userRoleRepository = new UserRoleRepositoryImpl();
    }

    @AfterClass
    public static void runAfterClass() {
        userRoleRepository = null;
    }

    @Test
    public void testGetAll(){
        List<UserRole> userRoleList = userRoleRepository.getAll();

        assertNotNull(userRoleList);
    }

    @Test
    public void testGetById(){
        UserRole userRole = userRoleRepository.getById(userRoleRepository.getAll().get(0).getId());

        assertNotNull(userRole);
    }
}
