package com.animals.app.controller.resource;

import com.animals.app.domain.*;
import com.animals.app.repository.AnimalRepository;
import com.animals.app.repository.Impl.AnimalBreedRepositoryImpl;
import com.animals.app.repository.Impl.AnimalRepositoryImpl;
import com.animals.app.repository.Impl.UserRepositoryImpl;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("admin")
public class AdminResource {
    private static Logger LOG = LogManager.getLogger(AdminResource.class);

    //return response with 400 code
    private final Response BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build();

    //return response with 404 code
    private final Response NOT_FOUND = Response.status(Response.Status.NOT_FOUND).build();

    private final String IMAGE_FOLDER = "images/"; //folder for animals images
    //size of fields in data base, table: animals

    private final int LENGTH_IMAGE = 50;

    /**
     * @param animalsFilter instance used for lookup.
     * @return list of animals.
     * -------------------------------------------------------------------
     * AnimalsFilter.page must be set and more than 0
     * AnimalsFilter.limit must be set and more than 0
     */
    @POST //http:localhost:8080/webapi/admin/animals
    @RolesAllowed("moderator")
    @Path("animals")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimals(@Valid @NotNull AnimalsFilter animalsFilter) {
        //get list of animals from data base
        AnimalRepository animalRepository = new AnimalRepositoryImpl();
        List<Animal> animals = animalRepository.getAdminAnimals(animalsFilter);

        //cast list of animals to generic list
        GenericEntity<List<Animal>> genericAnimals = new GenericEntity<List<Animal>>(animals) {};

        return ok(genericAnimals);
    }

    /**
     * @param animalsFilter instance used for lookup.
     * @return count of rows for pagination.
     */
    @POST //http:localhost:8080/webapi/animals/paginator
    @RolesAllowed("moderator")
    @Path("animals/paginator")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimalsPaginator(@Valid @NotNull AnimalsFilter animalsFilter) {
        //get count of row according to filter
        AnimalRepository animalRepository = new AnimalRepositoryImpl();
        long pages = animalRepository.getAdminAnimalsPaginator(animalsFilter);

        String json = "{\"rowsCount\" : " + String.valueOf(pages) + "}";

        return ok(json);
    }

    /**
     * @param animalId id of animal
     * @return return an animal instance from data base
     * -------------------------------------------------------------------
     * animalId must be set and more than 0
     */
    @GET //http:localhost:8080/webapi/animals/{animalId}
    @RolesAllowed({"moderator", "doctor"})
    @Path("animals/{animalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnimal(@PathParam("animalId") @NotNull @DecimalMin(value = "1") long animalId) {
        //get animal by id from data base
        AnimalRepository animalRepository = new AnimalRepositoryImpl();
        Animal animal = animalRepository.getById(animalId);

        if (animal == null) {
            return NOT_FOUND;
        }

        return ok(animal);
    }

    /**
     * Delete animal in data base
     * @param animalId id of animal
     * @return return response with status 200
     * -------------------------------------------------------------------
     * animalId must be set and more than 0
     */
    @DELETE //http:localhost:8080/webapi/animals/{animalId}
    @RolesAllowed("moderator")
    @Path("animals/{animalId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAnimal(@Context HttpServletRequest httpServlet,
                                 @PathParam("animalId") @NotNull @DecimalMin(value = "1") long animalId) {
        Animal animal;
        AnimalRepository animalRepository = new AnimalRepositoryImpl();
        String restPath = httpServlet.getServletContext().getRealPath("/"); //path to rest root folder

        try {
            animal = animalRepository.getById(animalId);
        } catch (PersistenceException e) {
            LOG.error(e);
            return NOT_FOUND;
        }

        //delete image
        if (animal.getImage() != null) {
            File file = new File(restPath + animal.getImage());
            if (file.exists()) {
                file.delete();
            }
        }

        //delete animal in data base by id
        animalRepository.delete(animalId);

        return ok();
    }

    /**
     * Update animal info in data base
     * @param animal instance to be updated
     * @return return response with status 200
     * -------------------------------------------------------------------
     * Animal.id must be set and more than 0
     * Animal.type.id must be set and more than 0
     * Animal.size must be set
     * Animal.dateOfRegister must be set
     * Animal.color must be set
     * Animal.address must be set
     * Animal.service must be set
     * Animal.service must be set
     */
    @POST //http:localhost:8080/webapi/animals/editor
    @RolesAllowed("moderator")
    @Path("animals/editor")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAnimal(@Context HttpServletRequest httpServlet, @Valid @NotNull Animal animal) {
        if (animal.getId() == null || animal.getType().getId() == null || animal.getService().getId() == null) {
            return BAD_REQUEST;
        }

        //check breed, if it new insert it into database
        saveNewBreed(animal.getBreed(), animal.getType());

        //save new image if set
        animal.setImage(saveNewImage(httpServlet.getServletContext().getRealPath("/"),
                animal.getImage(), animal.getId()));

        if ((animal.getImage() != null) && (animal.getImage().length() > LENGTH_IMAGE)) {
            return BAD_REQUEST;
        }

        //Update animal
        AnimalRepository animalRepository = new AnimalRepositoryImpl();
        try {
            animalRepository.update(animal);
        } catch (PersistenceException e) {
            LOG.error(e);
            return BAD_REQUEST;
        }

        //return relative image path to client
        String json;
        if (animal.getImage() == null) {
            json = "{\"filePath\":\"\"}";
        } else {
            json = "{\"filePath\":\"" + animal.getImage() + "\"}";
        }

        return ok(json);
    }

    /**
     * Delete animal in data base
     * @param animalId id of animal
     * @return return response with status 200 if ok
     * -------------------------------------------------------------------
     * animalId must be set and more than 0
     */
    @DELETE //http:localhost:8080/webapi/animals/image/{animalId}
    @RolesAllowed("moderator")
    @Path("animals/image/{animalId}")
    public Response deleteAnimalImage(@Context HttpServletRequest httpServlet,
                                      @PathParam("animalId") @DecimalMin(value = "1") long animalId) {
        Animal animal;

        AnimalRepository animalRepository = new AnimalRepositoryImpl();
        try {
            animal = animalRepository.getById(animalId);
        } catch (PersistenceException e) {
            LOG.error(e);
            return NOT_FOUND;
        }

        if (animal.getImage() == null) {
            return ok();
        }

        String restPath = httpServlet.getServletContext().getRealPath("/");         //path to rest root folder
        //delete image
        File file = new File(restPath + IMAGE_FOLDER + animal.getImage());
        if (file.exists()) {
            file.delete();
        }

        animal.setImage("");

        animalRepository.update(animal);

        return ok();
    }

    /**
     * @param usersFilter instance used for lookup.
     * @return count of rows for pagination.
     */
    @POST //http:localhost:8080/webapi/admin/users/paginator
    @Path("users/paginator")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAmountListUsersForAdmin(UsersFilter usersFilter) {

        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        //get count of row according to filter
        long pages = userRepository.getAdminUsersPaginator(usersFilter);

        if (pages == 0) {
            return NOT_FOUND;
        }

        String json = "{\"rowsCount\" : " + String.valueOf(pages) + "}";

//        return Response.status(Response.Status.OK).entity(str).build();
        return ok(json);
    }

    /**
     * @param usersFilter instance used for lookup.
     * @return list of users.
     */
    @POST //http:localhost:8080/webapi/admin/users
    @Path("users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllUsersForAdmin(UsersFilter usersFilter) {

        if (usersFilter == null) {
            return BAD_REQUEST;
        }
        if ((usersFilter.getPage() == 0) || (usersFilter.getLimit() == 0)) {
            return BAD_REQUEST;
        }

        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        //get list of users from data base
        List<User> users = userRepository.getAdminUsers(usersFilter);

        //cast list of animals to generic list
        GenericEntity<List<User>> genericUsers = new GenericEntity<List<User>>(users) {};

        if (genericUsers == null) {
            return NOT_FOUND;
        }

        //return Response.ok().entity(genericUsers).build();
        return ok(genericUsers);
    }

    /**
     * Return response with code 200(OK) and build returned entity
     * @return HTTP code K
     */
    private Response ok() {
        return Response.ok().build();
    }

    /**
     * Return response with code 200(OK) and build returned entity
     * @param entity Returned json instance from client
     * @return HTTP code K
     */
    private Response ok(Object entity) {
        return Response.ok().entity(entity).build();
    }

    private String saveNewImage(String rootFolder, String image, Long animalId) {
        if ((rootFolder == null) || (image == null) || (animalId == null)) {
            return image;
        }

        if (image.indexOf('\n') < 0) {
            return image;
        }

        String fileName = image.substring(0, image.indexOf('\n'));
        fileName = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date()) + fileName.substring(fileName.lastIndexOf('.'));
        image = image.substring(image.indexOf('\n') + 1);

        //get animal by id from database
        AnimalRepository animalRepository = new AnimalRepositoryImpl();
        Animal animal = animalRepository.getById(animalId);

        //delete old image
        File file = new File(rootFolder + animal.getImage());
        if (file.exists()) {
            file.delete();
        }

        byte[] decodedBytes = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            decodedBytes = decoder.decodeBuffer(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = new ByteArrayInputStream(decodedBytes);

        //Load and save image
        OutputStream out = null;
        try {
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(rootFolder + IMAGE_FOLDER + fileName));
            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            out.flush();
            out.close();
        } catch (IOException e) {
            LOG.error(e);
            return null;
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                LOG.error(ex);
                return null;
            }
        }

        return IMAGE_FOLDER + fileName;
    }

    private void saveNewBreed(AnimalBreed animalBreed, AnimalType animalType) {
        if ((animalBreed == null) || (animalType == null) || (animalType.getId() == null)) {
            return;
        }

        if ((animalBreed.getId() == null) &&
                ((animalBreed.getBreedUa() != null) || (animalBreed.getBreedEn() != null))) {
            animalBreed.setType(animalType);
            new AnimalBreedRepositoryImpl().insert_ua(animalBreed);
        }
    }
}
