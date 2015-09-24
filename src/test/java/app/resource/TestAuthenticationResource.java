package app.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.jersey.client.ClientResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.animals.app.domain.User;
import com.animals.app.domain.UserRole;
import com.animals.app.domain.UserType;
import com.animals.app.service.DateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.JNDIConfigurationForTests;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAuthenticationResource extends ResourceTestTemplate  {
	
	private static Client client;
	
	//moderator credentials
	private static final String LOGIN = "root";
	private static final String PASSWORD = "root";
	private static final String ROLE = "moderator";
	
	//Wrong credentials
	private static final String LOGINW = "rootWrong";
	private static final String PASSWORDW = "rootWrong";
	
	private String userLogin;
	
	private static final String REST_SERVICE_URL = BASE_URL + "account";	
	
	
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
    public void test01LoginToSite() {

    	String passwordMd5 = ResourceTestTemplate.getMd5(PASSWORD);
        String credentials = "Basic " + Base64.encodeBase64String((LOGIN + ':' + passwordMd5).getBytes());

        String result = client
                .target(REST_SERVICE_URL)
                .path("/login/OFF")
                .request()
                .header("Authorization", credentials)
                .post(null, String.class);

        Map<String, String> jsonMap = new Gson().fromJson(result, HashMap.class);

        String accessToken =  jsonMap.get("accessToken");
        String userLogin = jsonMap.get("socialLogin");
        String userRole = jsonMap.get("userRole"); 
        
        assertNotNull(accessToken);
        assertNotNull(result);
        
        assertEquals(LOGIN, userLogin);
        assertEquals(ROLE, userRole);       

    }
    
    @Test 
    @Ignore  //to Think about Why BadRequest?
    public void test02RefreshSession() {    	

    	
        String result = client
                .target(REST_SERVICE_URL)
                .path("/refresh")
                .request()                
                .get(String.class);   
        
        assertNotNull(result);  
        
    }
    
    
    @Test(expected = NotFoundException.class)
    public void test03LoginToSiteWrongCredentials() {

    	String passwordMd5 = ResourceTestTemplate.getMd5(PASSWORDW);
        String credentials = "Basic " + Base64.encodeBase64String((LOGINW + ':' + passwordMd5).getBytes());

        String result = client
                .target(REST_SERVICE_URL)
                .path("/login/OFF")
                .request()
                .header("Authorization", credentials)
                .post(null, String.class);             

    }
    
    @Test(expected = ServerErrorException.class)
    public void test04LoginToSiteWithoutHeader() {

    	String passwordMd5 = ResourceTestTemplate.getMd5(PASSWORDW);
        String credentials = "Basic " + Base64.encodeBase64String((LOGINW + ':' + passwordMd5).getBytes());

        String result = client
                .target(REST_SERVICE_URL)
                .path("/login/OFF")
                .request()                
                .post(null, String.class);             

    }
    
    @Test
    public void test05RegisterUser() {    	

    	userLogin = RandomStringUtils.random(10, true, true);

    	User user = createEmptyUser(userLogin);   	 
    	String json = new GsonBuilder()
    			.registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(user);      	
    	
        String result = client
                .target(REST_SERVICE_URL)
                .path("/registration/en")
                .request()                
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);             
                        
        assertNotNull(result);
    }
    
    @Test(expected = BadRequestException.class)
    public void test06LoginToSiteWithoutRegConfirmation() {

    	String passwordMd5 = ResourceTestTemplate.getMd5("11111");
        String credentials = "Basic " + Base64.encodeBase64String(("userToReg" + ':' + passwordMd5).getBytes());

        String result = client
                .target(REST_SERVICE_URL)
                .path("/login/OFF")
                .request()
                .header("Authorization", credentials)
                .post(null, String.class);             

    }
    
    @Test (expected = BadRequestException.class)
    public void test07RegisterUserLoginExist() {    	

    	User user = createEmptyUser(userLogin);  //userLogin="unknown"; 	 
    	String json = new GsonBuilder()
    			.registerTypeAdapter(Date.class, new DateSerializer())
                .create()
                .toJson(user); 	
    	
        String result = client
                .target(REST_SERVICE_URL)
                .path("/registration/en")
                .request()                
                .post(Entity.entity(json, MediaType.APPLICATION_JSON + ";charset=UTF-8"), String.class);        
        
                        
        
    }
    
    @Test (expected = BadRequestException.class)
    public void test08RefreshSessionWithError() {    	

    	
        String result = client
                .target(REST_SERVICE_URL)
                .path("/refresh")
                .request()                
                .get(String.class);   
        
    }
    
    @Test (expected = NotFoundException.class)
    public void test09RegConfirmationWithWrongVerificationCode() {    	

    	
        String result = client
                .target(REST_SERVICE_URL)
                .path("/confirmRegistration/userToReg/wrongCode")
                .request()                
                .post(null, String.class);                
        
    }
    
    @Test
    public void test10logout() { 
    	
        String result = client
                .target(REST_SERVICE_URL)
                .path("/logout")
                .request()                
                .get(String.class); 
        
        assertNotNull(result);
        
    }
    
    @Test
    public void test11loginOauthTwitterDirrect() { 
    	
    	String result = client
                .target(REST_SERVICE_URL)
                .path("/login/twitter_login_direct")
                .queryParam("token", "70100199-b2aQ9UqRiMCv2Qba2239Hume4YBOLRj3uI4TWUAQn")
                .queryParam("secret", "09Owdt8vE7OBnEErE2InI7h8u5tqrZ4yLynO2dx3jBKFf")
                .request() 
                .get(String.class);
        
    	assertNotNull(result);
    	
        
    }
    
    @Test
    @Ignore
    public void test11loginOauthTwitterDirrectWithResponse() { 
    	
    	ClientResponse responseMsg = client
                .target(REST_SERVICE_URL)
                .path("/login/twitter_login_direct")
                .queryParam("token", "70100199-b2aQ9UqRiMCv2Qba2239Hume4YBOLRj3uI4TWUAQn")
                .queryParam("secret", "09Owdt8vE7OBnEErE2InI7h8u5tqrZ4yLynO2dx3jBKFf")
                .request() 
                .get(ClientResponse.class);
        
    	assertNotNull(responseMsg);
    	assertEquals(200, responseMsg.getStatus());
        
    }
    
    @Test (expected = Exception.class)
    public void test12loginOauthTwitterDirrectWrongTokens() { 
    	
    	ClientResponse responseMsg = client
                .target(REST_SERVICE_URL)
                .path("/login/twitter_login_direct")
                .queryParam("token", "one")
                .queryParam("secret", "two")
                .request()                
                .get(ClientResponse.class);
    	
    }    
    
    private static User createEmptyUser(String userName) {
		
		User userToReg = new User();
		
		String userLogin;
		if (userName != null && !userName.isEmpty()) {
			userLogin = userName;
		} else {
			userLogin = "unknown";
		}
		
		userToReg.setEmail("yyy@rt.ua");
		userToReg.setName(userLogin);
		userToReg.setSocialLogin(userLogin);
		userToReg.setSurname(userLogin);		
		userToReg.setAddress(userLogin);
		userToReg.setPhone("000-0000001");
		userToReg.setOrganizationInfo("N/A");
		userToReg.setOrganizationName("N/A");
		userToReg.setPassword("13dde29e3e94c008d980dca4fce29e01");

		UserRole userRole = new UserRole();
		userRole.setRole("guest");
		userRole.setId(3);
		List<UserRole> list = new ArrayList<UserRole>();
		list.add(userRole);
		userToReg.setUserRole(list);

		UserType userType = new UserType();
		userType.setId(1);
		userToReg.setUserType(userType);
		
		userToReg.setRegistrationDate(new Date(System.currentTimeMillis()));
		
		return userToReg;
		
	}
    

}
