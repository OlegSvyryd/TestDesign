package com.animals.app.controller.resource;

import com.animals.app.domain.*;
import com.animals.app.repository.Impl.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.ws.rs.*;

import com.animals.app.service.CreateAnimalImage;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("animals")
@PermitAll
public class AnimalResource {
    private static Logger LOG = LogManager.getLogger(AnimalResource.class);

    private final Response BAD_REQUEST  = Response.status(Response.Status.BAD_REQUEST).build();
    private final Response NOT_FOUND    = Response.status(Response.Status.NOT_FOUND).build();
    private final Response SERVER_ERROR = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

    //path to target folder
    @Context
    private HttpServletRequest httpServlet;

    //repository instances
    private AnimalRepositoryImpl animalRepository = new AnimalRepositoryImpl();
    private AnimalStatusRepositoryImpl animalStatusRepository = new AnimalStatusRepositoryImpl();
    private AnimalStatusLogerRepositoryImpl animalStatusLogerRepository = new AnimalStatusLogerRepositoryImpl();
    private AnimalTypeRepositoryImpl animalTypeRepository = new AnimalTypeRepositoryImpl();
    private AnimalServiceRepositoryImpl animalServiceRepository = new AnimalServiceRepositoryImpl();
    private AnimalBreedRepositoryImpl animalBreedRepository = new AnimalBreedRepositoryImpl();

    @POST
    @Path("animal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertAnimal(@Valid Animal animal) {
        if (animal == null)
            return BAD_REQUEST;

        try {
            animal.setImage(getImageURL(animal.getImage()));
            animalRepository.insert(animal);
        } catch (IOException e) {
            LOG.error(e);
            return SERVER_ERROR;
        } catch (PersistenceException e) {
            LOG.error(e);
            return internalServerError(animal);
        }

        return ok(animal);
    }

    @POST
    @Path("adoption/pagenator")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAmountListForAdopting(AnimalsFilter animalsFilter) {
        long pages;
        try {
            pages = animalRepository.getAmountListForAdopting(animalsFilter);

            if (pages < 0)
                return BAD_REQUEST;
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        return ok(generateRowsCount(pages));
    }

    @POST
    @Path("adoption")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAnimalsForAdopting(AnimalsFilter animalsFilter) {
        GenericEntity<List<Animal>> genericAnimals;
        try {
            if (!animalsFilter.isAnimalFilterNotEmpty())
                return BAD_REQUEST;

            genericAnimals = new GenericEntity<List<Animal>>(animalRepository.getAllForAdopting(animalsFilter)) {};
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
            return NOT_FOUND;
        }

        return ok(genericAnimals);
    }

    @GET
    @Path("adoption/{animalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalStatus(@PathParam("animalId") String id) {
        GenericEntity<List<AnimalStatusLoger>> genericAnimals;
        try {
            genericAnimals = new GenericEntity<List<AnimalStatusLoger>>(
                    animalStatusLogerRepository.getAnimalStatusesByAnimalId(Long.parseLong(id))) {};
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
            return NOT_FOUND;
        }

        return ok(genericAnimals);
    }

    @POST
    @Path("found/pagenator")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAmountListFoundAnimals(AnimalsFilter animalsFilter) {
        long pages;
        try {
            pages = animalRepository.getAmountListFoundAnimals(animalsFilter);

            if (pages < 0)
                return BAD_REQUEST;
        } catch (PersistenceException e){
            LOG.error(e);
            return SERVER_ERROR;
        }

        return ok(generateRowsCount(pages));
    }

    @POST
    @Path("found")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFoundAnimals(AnimalsFilter animalsFilter) {
        GenericEntity<List<Animal>> genericAnimals;
        try {
            if (!animalsFilter.isAnimalFilterNotEmpty())
                return BAD_REQUEST;

            genericAnimals = new GenericEntity<List<Animal>>(animalRepository.getAllFoundAnimals(animalsFilter)) {};
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
            return NOT_FOUND;
        }

        return ok(genericAnimals);
    }

    @POST
    @Path("lost/pagenator")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAmountLostAnimals(AnimalsFilter animalsFilter) {
        long pages;
        try {
            pages = animalRepository.getAmountListLostAnimals(animalsFilter);

            if (pages < 0)
                return BAD_REQUEST;
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        return ok(generateRowsCount(pages));
    }

    @POST
    @Path("lost")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLostAnimals(AnimalsFilter animalsFilter) {
        GenericEntity<List<Animal>> genericAnimals;
        try {
            if (!animalsFilter.isAnimalFilterNotEmpty())
                return BAD_REQUEST;

            genericAnimals = new GenericEntity<List<Animal>>(animalRepository.getAllLostAnimals(animalsFilter)) {};
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
            return NOT_FOUND;
        }

        return ok(genericAnimals);
    }

    @GET
    @Path("service/{animalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShortInfo(@PathParam("animalId") String id) {
        Animal animalShortInfo;
        try {
            if (id == null)
                return BAD_REQUEST;

            animalShortInfo = animalRepository.getShortInfoById(Integer.parseInt(id));

            if (animalShortInfo == null)
                return NOT_FOUND;

        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        return ok(animalShortInfo);
    }

    @GET
    @Path("animal_types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalTypes() {
        GenericEntity<List<AnimalType>> genericAnimalTypes;
        try {
            genericAnimalTypes = new GenericEntity<List<AnimalType>>(animalTypeRepository.getAll()) {};
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
            return NOT_FOUND;
        }

        return ok(genericAnimalTypes);
    }

    @GET
    @Path("animal_services")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalServices() {
        GenericEntity<List<AnimalService>> genericAnimalServices;
        try {
            genericAnimalServices =
                    new GenericEntity<List<AnimalService>>(animalServiceRepository.getAll()) {};
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
            return NOT_FOUND;
        }

        return ok(genericAnimalServices);
    }

    @GET
    @Path("animal_breeds/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalBreadsByAnimalTypeId(@DecimalMin(value = "1") @Valid @PathParam("id") long animalTypeId) {
        GenericEntity<List<AnimalBreed>> genericAnimalBreeds;
        try {
            genericAnimalBreeds = new GenericEntity<List<AnimalBreed>>(animalBreedRepository.getByTypeId(animalTypeId)) {};
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
            return NOT_FOUND;
        }

        return ok(genericAnimalBreeds);
    }

    @GET
    @Path("medical_history/types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalMedicalHistoryTypes() {
        GenericEntity<List<AnimalStatus>> genericAnimalStatuses;
        try {
            genericAnimalStatuses =
                    new GenericEntity<List<AnimalStatus>>(animalStatusRepository.getAll()) {};
        } catch (PersistenceException e) {
            LOG.error(e);
            return SERVER_ERROR;
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
            return NOT_FOUND;
        }

        return ok(genericAnimalStatuses);
    }

    /**
     * Keep image URL for inserting into database
     * @param image Image of bytes
     * @return Image URL
     */
    private String getImageURL(String image) throws IOException{
        return "images/" + CreateAnimalImage.createAnimalImage(image, httpServlet.getServletContext().getRealPath("/") + "images/");
    }

    /**
     * Generate JSON that contain amount rows some instance from database
     * @param pages Amount records into database
     * @return JSON object of amount rows
     */
    private String generateRowsCount(long pages){
        return "{\"rowsCount\" : " + String.valueOf(pages) + "}";
    }

    /**
     * Return response with code 200(OK) and build returned entity
     *
     * @param entity Returned json instance from client
     * @return HTTP code K
     */
    private Response ok(Object entity) {
        return Response.ok().entity(entity).build();
    }

    /**
     * Return response with code 500(Internal Server Error) and build returned entity
     *
     * @param entity Returned json instance from client
     * @return HTTP code K
     */
    private Response internalServerError(Object entity) {
        return Response.serverError().entity(entity).build();
    }
}