/*
 * Create user with login - root and password - root and role admin or change credentials in
 * these values: LOGIN, PASSWORD for this test be passed
 */
package app.resource;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.User;
import com.animals.app.repository.Impl.UserRepositoryImpl;
import com.animals.app.repository.Impl.UserRoleRepositoryImpl;
import com.animals.app.repository.Impl.UserTypeRepositoryImpl;
import com.animals.app.repository.UserRepository;
import com.animals.app.service.DateSerializer;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Created by oleg on 23.09.2015.
 */
@Category(IntegrationTest.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserResource extends ResourceTestTemplate {
    private static Logger LOG = LogManager.getLogger(TestAdminResource.class);

    private static Client client;

    private static final String LOGIN = "root";
    private static final String PASSWORD = "root";
    private static String accessToken;
    private static User user;

    //size of fields in data base, table: users
    private final int LENGTH_NAME = 35;
    private final int LENGTH_SURNAME = 45;
    private final int LENGTH_PHONE = 15;
    private final int LENGTH_EMAIL = 30;
    private final int LENGTH_ADDRESS = 120;
    private final int LENGTH_PASSWORD = 50;
    private final int LENGTH_SOCIAL_LOGIN = 50;
    private final int LENGTH_ORGANIZATION_NAME = 70;
    private final int LENGTH_ORGANIZATION_INFO = 100;

    private static final String REST_SERVICE_URL = BASE_URL + "user";

    @BeforeClass
    public static void runBeforeClass() {
        JNDIConfigurationForTests.configureJNDIForJUnit();

        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void runAfterClass() {
        client = null;
        user = null;
    }

    @Test
    public void test00Initialization() {
        accessToken = login(client, LOGIN, PASSWORD);

        assertNotNull(accessToken);

        UserRepository userRepository = new UserRepositoryImpl();

        user = new User();
        user.setName(RandomStringUtils.random(15, true, true));
        user.setSurname(RandomStringUtils.random(15, true, true));
        user.setPhone(RandomStringUtils.random(15, true, true));
        user.setUserType(new UserTypeRepositoryImpl().getAll().get(0));
        user.setUserRole(new UserRoleRepositoryImpl().getAll().subList(0, 1));
        user.setRegistrationDate(new Date(System.currentTimeMillis()));
        user.setEmail(RandomStringUtils.random(10, true, true));
        user.setAddress(RandomStringUtils.random(10, true, true));
        user.setSocialLogin(RandomStringUtils.random(15, true, true));
        user.setPassword(RandomStringUtils.random(10, true, true));

        userRepository.insert(user);

        assertNotNull(user.getId());
    }

    /*
     * Testing of getting user
     * Path: /admin/users/userId
     * Method: get
     * Send: valid userId
     * Expect: instance of user
     */
    @Ignore
    @Test
    public void test01GetUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User expected = client
                .target(REST_SERVICE_URL)
                .path("admin/users" + user.getId())
                .request()
                .header("AccessToken", accessToken)
                .get(User.class);

        assertNotNull(expected);
    }

    /*
     * Testing of getting user
     * Path: /admin/users/userId
     * Method: get
     * Send: not valid userId (userId = -1)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test02GetUser() {
        assertNotNull(accessToken);

        client.target(REST_SERVICE_URL)
                .path("users/-1")
                .request()
                .header("AccessToken", accessToken)
                .get(User.class);
    }

    /*
     * Testing of getting user
     * Path: /admin/users/userId
     * Method: get
     * Send: not valid userId (userId = 0)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test03GetUser() {
        assertNotNull(accessToken);

        client.target(REST_SERVICE_URL)
                .path("users/0")
                .request()
                .header("AccessToken", accessToken)
                .get(User.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user = null)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test04UpdateUser() {
        assertNotNull(accessToken);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(null, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.id = null)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test05UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        actual.setId(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test05UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.id = -1)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test06UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        actual.setId(new Integer(-1));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test06UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.id = 0)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test07UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        actual.setId(new Integer(0));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test07UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.type.id = null)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test08UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        actual.getUserType().setId(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test08UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.type.id = -1)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test09UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        actual.getUserType().setId(new Integer(-1));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test09UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.type.id = 0)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test10UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        actual.getUserType().setId(new Integer(0));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test10UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.user = null)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test11UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        actual.setEmail(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test11UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.user.id = null)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test12UpdateUser() {
        assertNotNull(accessToken);

        User actual = SerializationUtils.clone(user);
        actual.setEmail(new User().getEmail());
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test12UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.user.id = -1)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test13UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        User user = new User();
        user.setId(new Integer(-1));
        actual.setEmail(user.getEmail());
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test13UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.user.id = 0)
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test14UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        User user = new User();
        user.setId(new Integer(0));
        actual.setEmail(user.getEmail());
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test14UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.user.id = max int value (no such user in data base))
     * Expect: response with status 400
     */
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test15UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getUserType());
        assertNotNull(user.getUserRole());
        assertNotNull(user.getRegistrationDate());
        assertNotNull(user.getPhone());
        assertNotNull(user.getAddress());
        assertNotNull(user.getPassword());
        assertNotNull(user.getEmail());
        assertNotNull(user.getSocialLogin());

        User actual = SerializationUtils.clone(user);
        User user = new User();
        user.setId(Integer.MAX_VALUE);
        actual.setEmail(user.getEmail());
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test15UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }


    /*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.service = null)
     * Expect: response with status 400
     */
/*
    @Ignore
    @Test(expected = BadRequestException.class)
    public void test16UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setService(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test16UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.service.id = null)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test17UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setService(new UserService());
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test17UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.service.id = -1)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test18UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        UserService userService = new UserService();
        userService.setId(new Long(-1));
        actual.setService(userService);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test18UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.service.id = 0)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test19UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        UserService userService = new UserService();
        userService.setId(new Long(0));
        actual.setService(userService);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test19UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.service.id = max long value (no such service in data base))
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test20UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        UserService userService = new UserService();
        userService.setId(new Long(Long.MAX_VALUE));
        actual.setService(userService);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test20UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.sex = null)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test21UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setSex(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test21UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.size = null)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test22UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setSize(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test22UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (length of user.transpNumber = LENGTH_TRANSPNUMBER + 1, max = LENGTH_TRANSPNUMBER)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test23UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setTranspNumber(RandomStringUtils.random(LENGTH_TRANSPNUMBER + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test23UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (length of user.tokenNumber = LENGTH_TOKENNUMBER + 1, max = LENGTH_TOKENNUMBER)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test24UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setTokenNumber(RandomStringUtils.random(LENGTH_TOKENNUMBER + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test24UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.dateOfRegister = null)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test25UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setDateOfRegister(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test25UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (length of user.color = LENGTH_COLOR + 1, max = LENGTH_COLOR)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test26UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setColor(RandomStringUtils.random(LENGTH_COLOR + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test26UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (length of user.description = LENGTH_DESCRIPTION + 1, max = LENGTH_DESCRIPTION)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test27UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setDescription(RandomStringUtils.random(LENGTH_DESCRIPTION + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test27UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (length of user.address = LENGTH_ADDRESS + 1, max = LENGTH_ADDRESS)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test28UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setAddress(RandomStringUtils.random(LENGTH_ADDRESS + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test28UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (user.address = null)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test29UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setAddress(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test29UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    */
/*
     * Testing of updating user
     * Path: /admin/users/editor
     * Method: post
     * Send: not valid user (length of user.image = LENGTH_IMAGE + 1, max = LENGTH_IMAGE)
     * Expect: response with status 400
     *//*

    @Test(expected = BadRequestException.class)
    public void test30UpdateUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getSex());
        assertNotNull(user.getType());
        assertNotNull(user.getType().getId());
        assertNotNull(user.getSize());
        assertNotNull(user.getDateOfRegister());
        assertNotNull(user.getColor());
        assertNotNull(user.getAddress());
        assertNotNull(user.getService());
        assertNotNull(user.getService().getId());

        User actual = SerializationUtils.clone(user);
        actual.setImage(RandomStringUtils.random(LENGTH_IMAGE + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test30UpdateUser - " + json);

        client.target(REST_SERVICE_URL)
                .path("users/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }
*/

    /*
     * Testing of deleting user
     * Path: /admin/users/userId
     * Method: delete
     * Send: not valid userId (userId = -1)
     * Expect: response with status 400
     */
    @Ignore
    @Test
    public void test31DeleteUser() {
        assertNotNull(accessToken);

        Response response = client
                .target(REST_SERVICE_URL)
                .path("users/-1")
                .request()
                .header("AccessToken", accessToken)
                .delete();

        assertNotNull(response);
        assertEquals(response.getStatus(), 400);
    }

    /*
     * Testing of deleting user
     * Path: /admin/users/userId
     * Method: delete
     * Send: not valid userId (userId = 0)
     * Expect: response with status 400
     */
    @Ignore
    @Test
    public void test32DeleteUser() {
        assertNotNull(accessToken);

        Response response = client
                .target(REST_SERVICE_URL)
                .path("users/0")
                .request()
                .header("AccessToken", accessToken)
                .delete();

        assertNotNull(response);
        assertEquals(response.getStatus(), 400);
    }

    /*
     * Testing of deleting user
     * Path: /admin/users/userId
     * Method: delete
     * Send: valid userId
     * Expect: response with status 200
     */
    @Ignore
    @Test
    public void test33DeleteUser() {
        assertNotNull(accessToken);
        assertNotNull(user);
        assertNotNull(user.getId());

        Response response = client
                .target(REST_SERVICE_URL)
                .path("users/" + user.getId())
                .request()
                .header("AccessToken", accessToken)
                .delete();

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
    }
}