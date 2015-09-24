package app.resource;

import com.animals.app.service.AuthorizationFilter;
import com.animals.app.service.ValidationFilterDomainFields;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import javax.ws.rs.client.Client;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Rostyslav.Viner on 17.09.2015.
 */
public class ResourceTestTemplate extends JerseyTest {
    protected static final String BASE_URL = "http://localhost:9998/";
    private static final String REST_LOGIN_URL = BASE_URL + "account";

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    protected DeploymentContext configureDeployment() {
        ResourceConfig config = new ValidationFilterDomainFields();
        config.register(AuthorizationFilter.class);
        return ServletDeploymentContext.forServlet(
                new ServletContainer(config))
                .build();
    }

    public String login(Client client, String login, String password) {
        String passwordMd5 = getMd5(password);
        String credentials = "Basic " + Base64.encodeBase64String((login + ':' + passwordMd5).getBytes());

        String result = client
                .target(REST_LOGIN_URL)
                .path("/login/OFF")
                .request()
                .header("Authorization", credentials)
                .post(null, String.class);

        Map<String, String> jsonMap = new Gson().fromJson(result, HashMap.class);

        return jsonMap.get("accessToken");
    }

    public static String getMd5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
