/*
 * Create user with login - root and password - root and role admin or change credentials in
 * these values: LOGIN, PASSWORD for this test be passed
 */
package app.resource;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.Animal;
import com.animals.app.domain.AnimalService;
import com.animals.app.domain.User;
import com.animals.app.repository.AnimalRepository;
import com.animals.app.repository.Impl.AnimalRepositoryImpl;
import com.animals.app.repository.Impl.AnimalServiceRepositoryImpl;
import com.animals.app.repository.Impl.AnimalTypeRepositoryImpl;
import com.animals.app.service.DateSerializer;
import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Rostyslav.Viner on 03.09.2015.
 */
@Category(IntegrationTest.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAdminResource extends ResourceTestTemplate {
    private static Logger LOG = LogManager.getLogger(TestAdminResource.class);

    private static Client client;

    private static final String LOGIN = "root";
    private static final String PASSWORD = "root";
    private static String accessToken;
    private static Animal animal;

    //size of fields in data base, table: animals
    private final int LENGTH_TRANSPNUMBER = 15;
    private final int LENGTH_TOKENNUMBER = 12;
    private final int LENGTH_COLOR = 20;
    private final int LENGTH_DESCRIPTION = 100;
    private final int LENGTH_ADDRESS = 120;
    private final int LENGTH_IMAGE = 50;

    private static final String REST_SERVICE_URL = BASE_URL + "admin";

    @BeforeClass
    public static void runBeforeClass() {
        JNDIConfigurationForTests.configureJNDIForJUnit();

        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void runAfterClass() {
        client = null;
        animal = null;
    }

    @Test
    public void test00Initialization() {
        accessToken = login(client, LOGIN, PASSWORD);

        assertNotNull(accessToken);

        AnimalRepository animalRepository = new AnimalRepositoryImpl();

        animal = new Animal();
        animal.setSex(Animal.SexType.FEMALE);
        animal.setType(new AnimalTypeRepositoryImpl().getAll().get(0));
        animal.setSize(Animal.SizeType.LARGE);
        animal.setDateOfRegister(new Date(System.currentTimeMillis()));
        animal.setColor(RandomStringUtils.random(10, true, true));
        animal.setAddress(RandomStringUtils.random(10, true, true));
        animal.setService(new AnimalServiceRepositoryImpl().getAll().get(0));

        animalRepository.insert(animal);

        assertNotNull(animal.getId());
    }

    /*
     * Testing of getting animal
     * Path: /admin/animals/animalId
     * Method: get
     * Send: valid animalId
     * Expect: instance of animal
     */
    @Test
    public void test01GetAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal expected = client
                .target(REST_SERVICE_URL)
                .path("animals/" + animal.getId())
                .request()
                .header("AccessToken", accessToken)
                .get(Animal.class);

        assertNotNull(expected);
    }

    /*
     * Testing of getting animal
     * Path: /admin/animals/animalId
     * Method: get
     * Send: not valid animalId (animalId = -1)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test02GetAnimal() {
        assertNotNull(accessToken);

        client.target(REST_SERVICE_URL)
                .path("animals/-1")
                .request()
                .header("AccessToken", accessToken)
                .get(Animal.class);
    }

    /*
     * Testing of getting animal
     * Path: /admin/animals/animalId
     * Method: get
     * Send: not valid animalId (animalId = 0)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test03GetAnimal() {
        assertNotNull(accessToken);

        client.target(REST_SERVICE_URL)
                .path("animals/0")
                .request()
                .header("AccessToken", accessToken)
                .get(Animal.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test04UpdateAnimal() {
        assertNotNull(accessToken);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(null, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.id = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test05UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setId(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test05UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.id = -1)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test06UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setId(new Long(-1));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test06UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.id = 0)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test07UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setId(new Long(0));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test07UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.type.id = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test08UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.getType().setId(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test08UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.type.id = -1)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test09UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.getType().setId(new Long(-1));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test09UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.type.id = 0)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test10UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.getType().setId(new Long(0));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test10UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.user = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test11UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setUser(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test11UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.user.id = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test12UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setUser(new User());
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test12UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.user.id = -1)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test13UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        User user = new User();
        user.setId(new Integer(-1));
        actual.setUser(user);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test13UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.user.id = 0)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test14UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        User user = new User();
        user.setId(new Integer(0));
        actual.setUser(user);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test14UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.user.id = max int value (no such user in data base))
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test15UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        User user = new User();
        user.setId(Integer.MAX_VALUE);
        actual.setUser(user);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test15UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }


    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.service = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test16UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setService(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test16UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.service.id = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test17UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setService(new AnimalService());
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test17UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.service.id = -1)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test18UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        AnimalService animalService = new AnimalService();
        animalService.setId(new Long(-1));
        actual.setService(animalService);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test18UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.service.id = 0)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test19UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        AnimalService animalService = new AnimalService();
        animalService.setId(new Long(0));
        actual.setService(animalService);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test19UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.service.id = max long value (no such service in data base))
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test20UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        AnimalService animalService = new AnimalService();
        animalService.setId(new Long(Long.MAX_VALUE));
        actual.setService(animalService);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test20UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.sex = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test21UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setSex(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test21UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.size = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test22UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setSize(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test22UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (length of animal.transpNumber = LENGTH_TRANSPNUMBER + 1, max = LENGTH_TRANSPNUMBER)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test23UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setTranspNumber(RandomStringUtils.random(LENGTH_TRANSPNUMBER + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test23UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (length of animal.tokenNumber = LENGTH_TOKENNUMBER + 1, max = LENGTH_TOKENNUMBER)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test24UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setTokenNumber(RandomStringUtils.random(LENGTH_TOKENNUMBER + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test24UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.dateOfRegister = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test25UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setDateOfRegister(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test25UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (length of animal.color = LENGTH_COLOR + 1, max = LENGTH_COLOR)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test26UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setColor(RandomStringUtils.random(LENGTH_COLOR + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test26UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (length of animal.description = LENGTH_DESCRIPTION + 1, max = LENGTH_DESCRIPTION)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test27UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setDescription(RandomStringUtils.random(LENGTH_DESCRIPTION + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test27UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (length of animal.address = LENGTH_ADDRESS + 1, max = LENGTH_ADDRESS)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test28UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setAddress(RandomStringUtils.random(LENGTH_ADDRESS + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test28UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (animal.address = null)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test29UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setAddress(null);
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test29UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of updating animal
     * Path: /admin/animals/editor
     * Method: post
     * Send: not valid animal (length of animal.image = LENGTH_IMAGE + 1, max = LENGTH_IMAGE)
     * Expect: response with status 400
     */
    @Test(expected = BadRequestException.class)
    public void test30UpdateAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());
        assertNotNull(animal.getSex());
        assertNotNull(animal.getType());
        assertNotNull(animal.getType().getId());
        assertNotNull(animal.getSize());
        assertNotNull(animal.getDateOfRegister());
        assertNotNull(animal.getColor());
        assertNotNull(animal.getAddress());
        assertNotNull(animal.getService());
        assertNotNull(animal.getService().getId());

        Animal actual = SerializationUtils.clone(animal);
        actual.setImage(RandomStringUtils.random(LENGTH_IMAGE + 1, true, true));
        String json = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(actual);

        LOG.debug("TestName: test30UpdateAnimal - " + json);

        client.target(REST_SERVICE_URL)
                .path("animals/editor")
                .request()
                .header("AccessToken", accessToken)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);
    }

    /*
     * Testing of deleting animal
     * Path: /admin/animals/animalId
     * Method: delete
     * Send: not valid animalId (animalId = -1)
     * Expect: response with status 400
     */
    @Test
    public void test31DeleteAnimal() {
        assertNotNull(accessToken);

        Response response = client
                .target(REST_SERVICE_URL)
                .path("animals/-1")
                .request()
                .header("AccessToken", accessToken)
                .delete();

        assertNotNull(response);
        assertEquals(response.getStatus(), 400);
    }

    /*
     * Testing of deleting animal
     * Path: /admin/animals/animalId
     * Method: delete
     * Send: not valid animalId (animalId = 0)
     * Expect: response with status 400
     */
    @Test
    public void test32DeleteAnimal() {
        assertNotNull(accessToken);

        Response response = client
                .target(REST_SERVICE_URL)
                .path("animals/0")
                .request()
                .header("AccessToken", accessToken)
                .delete();

        assertNotNull(response);
        assertEquals(response.getStatus(), 400);
    }

    /*
     * Testing of deleting animal
     * Path: /admin/animals/animalId
     * Method: delete
     * Send: valid animalId
     * Expect: response with status 200
     */
    @Test
    public void test33DeleteAnimal() {
        assertNotNull(accessToken);
        assertNotNull(animal);
        assertNotNull(animal.getId());

        Response response = client
                .target(REST_SERVICE_URL)
                .path("animals/" + animal.getId())
                .request()
                .header("AccessToken", accessToken)
                .delete();

        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
    }
}