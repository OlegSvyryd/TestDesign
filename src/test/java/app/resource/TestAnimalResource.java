package app.resource;

import app.JNDIConfigurationForTests;
import com.animals.app.domain.AnimalBreed;
import com.animals.app.domain.AnimalService;
import com.animals.app.domain.AnimalStatus;
import com.animals.app.domain.AnimalType;
import com.animals.app.service.ValidationFilterDomainFields;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Rostyslav.Viner on 03.09.2015.
 */
@Category(IntegrationTest.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAnimalResource extends ResourceTestTemplate {
    private static Client client;

    private static AnimalType animalType;

    private static final String REST_SERVICE_URL = BASE_URL + "animals";

    @BeforeClass
    public static void runBeforeClass() {
        JNDIConfigurationForTests.configureJNDIForJUnit();

        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void runAfterClass() {
        client = null;
    }

    @Test
    public void test01GetAnimalTypes() {

        List<AnimalType> animalTypes = client
                .target(REST_SERVICE_URL)
                .path("/animal_types")
                .request()
                .get(new GenericType<List<AnimalType>>() {});

        assertNotNull(animalTypes);
        assertNotEquals(animalTypes.size(), 0);

        animalType = animalTypes.get(0);
    }

    @Test
    public void test02GetAnimalBreeds() {
        assertNotNull(animalType);
        assertNotNull(animalType.getId());

        List<AnimalBreed> animalBreeds = client
                .target(REST_SERVICE_URL)
                .path("/animal_breeds/" + animalType.getId())
                .request()
                .get(new GenericType<List<AnimalBreed>>() {});

        assertNotNull(animalBreeds);
        assertNotEquals(animalBreeds.size(), 0);
    }

    @Test(expected = BadRequestException.class)
    public void test03GetAnimalBreeds() {

        client.target(REST_SERVICE_URL)
                .path("/animal_breeds/0")
                .request()
                .get(new GenericType<List<AnimalBreed>>() {});
    }

    @Test(expected = BadRequestException.class)
    public void test04GetAnimalBreeds() {

        client.target(REST_SERVICE_URL)
                .path("/animal_breeds/-1")
                .request()
                .get(new GenericType<List<AnimalBreed>>() {});
    }

    @Test
    public void test05GetAnimalServices() {

        List<AnimalService> animalServices = client
                .target(REST_SERVICE_URL)
                .path("/animal_services")
                .request()
                .get(new GenericType<List<AnimalService>>() {});

        assertNotNull(animalServices);
        assertNotEquals(animalServices.size(), 0);
    }

    @Test
    public void test06GetAnimalMedicalHistoryTypes() {

        List<AnimalStatus> animalStatuses = client
                .target(REST_SERVICE_URL)
                .path("/medical_history/types")
                .request()
                .get(new GenericType<List<AnimalStatus>>() {});

        assertNotNull(animalStatuses);
        assertNotEquals(animalStatuses.size(), 0);
    }
}
