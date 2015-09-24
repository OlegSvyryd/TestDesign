package com.animals.app.controller.resource;

import com.animals.app.domain.Animal;
import com.animals.app.repository.AnimalRepository;
import com.animals.app.repository.Impl.AnimalRepositoryImpl;
import com.animals.app.service.Facebook;
import com.animals.app.service.Twitt;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by aquaneo on 8/19/2015.
 */
@Path("socials")
//@RolesAllowed("модератор")
public class SocialsResource {
    private static Logger LOG = LogManager.getLogger(SocialsResource.class);

    private final Response BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build();

    private final Response NOT_FOUND = Response.status(Response.Status.NOT_FOUND).build();

    private final Response OK = Response.status(Response.Status.OK).build();

    @Context
    private HttpServletRequest httpServlet;

    @POST //http:localhost:8080/webapi/socials/twitter/animalId
    @Path("/twitter/{animalId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response sendTwitt (@PathParam("animalId") long animalId) {

        Twitt twitt;
        String consumerKey, consumerSecret, accessToken, accessTokenSecret, messageBody ;

        System.out.println("animalId -" + animalId);
        if (animalId == 0) {
            return BAD_REQUEST;
        }
        consumerKey = socialsConfig.getProperty("twitter.consumerKey");
        consumerSecret = socialsConfig.getProperty("twitter.consumerSecret");
        accessToken = socialsConfig.getProperty("twitter.accessToken");
        accessTokenSecret = socialsConfig.getProperty("twitter.accessTokenSecret");
        messageBody = socialsConfig.getProperty("twitter.messageBody");

        //get animal by id from database
        AnimalRepository animalRepository = new AnimalRepositoryImpl();
        Animal animal = animalRepository.getById(animalId);

        if (animal == null) {
            return NOT_FOUND;
        }
        if (animal.getDateOfTwitter() == null) {
            twitt = new Twitt();
            twitt.setMessage("Нова тварина! Деталі: - http://tym.dp.ua/#/ua/animal/adoption/" + animalId);

            //attach any media, if you want to
            if (!(animal.getImage() == null)) {

                String restPath = httpServlet.getServletContext().getRealPath("/"); //path to rest root folder
                twitt.setMedia(restPath + animal.getImage());
            }


            if (twitt.sendTwitt(consumerKey, consumerSecret, accessToken, accessTokenSecret)) {
                animal.setDateOfTwitter(getCurrentDate());
                System.out.println("Date: - " + animal.getDateOfTwitter().toString());
                animalRepository.twitterUpdate(animal);
                return OK;
            } else
                return BAD_REQUEST;
        }
        else
            return BAD_REQUEST;
    }

    @POST //http:localhost:8080/webapi/socials/facebook/animalId
    @Path("/facebook/{animalId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response sendFacebook (@PathParam("animalId") long animalId) {
        Facebook facebook;
        String accessToken, wallId ;

        System.out.println("animalId -" + animalId);
        if (animalId == 0) {
            return BAD_REQUEST;
        }

        //get animal by id from database
        AnimalRepository animalRepository = new AnimalRepositoryImpl();
        Animal animal = animalRepository.getById(animalId);

        if (animal == null) {
            return NOT_FOUND;
        }

        if (animal.getDateOfFacebook() == null) {

            accessToken = socialsConfig.getProperty("facebook.accessToken");
            wallId = socialsConfig.getProperty("facebook.wallId");

            //attach any media, if you want to
//            if (!animal.getImage().equals(null)) {
  //              String restPath = httpServlet.getServletContext().getRealPath("/"); //path to rest root folder
    //            twitt.setMedia(restPath + animal.getImage());
      //      }

                facebook = new Facebook();


            if (facebook.sendFacebook(accessToken, wallId, getCurrentDate().toString()+animalId)) {
                animal.setDateOfFacebook(getCurrentDate());
                System.out.println("Date: - " + animal.getDateOfFacebook().toString());
                animalRepository.facebookUpdate(animal);
                return OK;
            } else
                return BAD_REQUEST;
        }
        else
            return BAD_REQUEST;

    }

    private static java.sql.Date getCurrentDate() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }


    /**
     * Return response with code 200(OK) and build returned entity
     * @param entity Returned json instance from client
     * @return HTTP code K
     */
    private Response ok(Object entity) {
        return Response.ok().entity(entity).build();
    }

    private static Properties socialsConfig = new Properties();
    {
        fetchConfig();
    }

    /**
     * Open a specific text file containing reCAPTCHA secret key
     * and verification URL.
     */
    private void fetchConfig() {
        //This file contains the reCAPTCHA config properties mentioned above.
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("project.properties").getFile());

        try (InputStream input = new FileInputStream(file)) {
            socialsConfig.load(input);
        }
        catch (IOException ex){
            System.err.println("Cannot open and load mail server properties file. Put it on...");
        }
    }
}
