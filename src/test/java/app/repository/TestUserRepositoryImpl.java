package app.repository;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.User;
import com.animals.app.repository.Impl.UserRepositoryImpl;
import com.animals.app.repository.Impl.UserRoleRepositoryImpl;
import com.animals.app.repository.Impl.UserTypeRepositoryImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.sql.Date;
import java.util.List;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * Created by oleg on 24.07.2015.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserRepositoryImpl extends JNDIConfigurationForTests {

    private static UserRepositoryImpl userRepository;
    private static User actual;

    @BeforeClass
    public static void runBeforeClass() {
        configureJNDIForJUnit();

        userRepository = new UserRepositoryImpl();

        actual = new User();
        actual.setName(RandomStringUtils.random(10, true, true));
        actual.setSurname(RandomStringUtils.random(10, true, true));
        actual.setRegistrationDate(new Date(System.currentTimeMillis()));
        actual.setUserType(new UserTypeRepositoryImpl().getAll().get(0));
        actual.setUserRole(new UserRoleRepositoryImpl().getAll().subList(0,1));
        actual.setPhone(RandomStringUtils.random(10, true, true));
        actual.setAddress(RandomStringUtils.random(10, true, true));
        actual.setEmail(RandomStringUtils.random(10, true, true));
        actual.setSocialLogin(RandomStringUtils.random(10, true, true));
        actual.setPassword(RandomStringUtils.random(10, true, true));
        actual.setOrganizationName(RandomStringUtils.random(10, true, true));
        actual.setOrganizationInfo(RandomStringUtils.random(10, true, true));
        actual.setActive(true);
    }

    @AfterClass
    public static void runAfterClass() {
        userRepository = null;
        actual = null;
    }

    @Test
    public void test01Insert() {
        assertNull(actual.getId());

        userRepository.insert(actual);

        assertNotNull(actual.getId());
    }

    @Test
    public void test02GetAll(){
        List<User> userList = userRepository.getAll();

        assertNotNull(userList);
    }

    @Test
    public void test03GetById(){
        User user = userRepository.getById(actual.getId());

        assertNotNull(user);
    }

    @Test
    public void test04Update() {
        User expected = userRepository.getById(actual.getId());

        assertNotNull(expected);

        expected.setName(RandomStringUtils.random(10, true, true));
        expected.setSurname(RandomStringUtils.random(10, true, true));
        expected.setEmail(RandomStringUtils.random(10, true, true));
        expected.setPassword(RandomStringUtils.random(10, true, true));
        expected.setActive(false);
        expected.setPhone(RandomStringUtils.random(10, true, true));

        userRepository.update(expected);

        assertNotNull(expected);
        assertNotSame(expected, actual);
    }

    @Test
    public void test05Delete() {
        userRepository.delete(actual.getId());

        User expected = userRepository.getById(actual.getId());

        assertNull(expected);
    }
}
