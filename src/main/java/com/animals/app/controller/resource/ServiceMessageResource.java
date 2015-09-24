package com.animals.app.controller.resource;

import com.animals.app.service.Feedback;
import com.animals.app.service.MailSender;
import com.animals.app.service.ServiceMessage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

/**
 * Created by Vova on 09.09.2015.
 */

@Path("service")
public class ServiceMessageResource {
    private static Logger LOG = LogManager.getLogger(FeedbackResource.class);

    private String url, serverSecretKey ;

    private final Response BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build();

    private final Response NOT_FOUND = Response.status(Response.Status.NOT_FOUND).build();

    @POST
    @Path("message")//http:localhost:8080/webapi/service/message
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response sendMessage (ServiceMessage message) {
        String from, text, sender, service, animalId;

        from = message.getEmail();
        text = message.getText();
        sender = message.getSignup();
        animalId = message.getAnimalId();
        service = message.getService();

        if (from == null || from.isEmpty() || text == null || text.isEmpty() || sender == null || sender.isEmpty() || animalId == null || service == null )
            return BAD_REQUEST;

        url = reCaptchaConfig.getProperty("recaptcha.url");
        serverSecretKey = reCaptchaConfig.getProperty("recaptcha.serversecretkey");

        JsonObject response = validateCaptcha(serverSecretKey, message.getgRecaptchaResponse());


        MailSender mail = new MailSender();
        mail.serviceMessageSend(from, text, sender, animalId, service);

        return ok(response.toString());

    }

    /**
     * Return response with code 200(OK) and build returned entity
     * @param entity Returned json instance from client
     * @return HTTP code K
     */
    private Response ok(Object entity) {
        return Response.ok().entity(entity).build();
    }

    private JsonObject validateCaptcha(String secret, String response)
    {
        JsonObject jsonObject = null;
        URLConnection connection = null;
        InputStream is = null;
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        try {
            String query = String.format("secret=%s&response=%s",
                    URLEncoder.encode(secret, charset),
                    URLEncoder.encode(response, charset));

            connection = new URL(url + "?" + query).openConnection();
            is = connection.getInputStream();
            JsonReader rdr = Json.createReader(is);
            jsonObject = rdr.readObject();

        } catch (IOException ex) {

        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }

            }
        }
        return jsonObject;
    }

    private static Properties reCaptchaConfig = new Properties();
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
            reCaptchaConfig.load(input);
        }
        catch (IOException ex){
            System.err.println("Cannot open and load mail server properties file. Put it on...");
        }
    }
}


