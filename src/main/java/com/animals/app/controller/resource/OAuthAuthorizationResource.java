package com.animals.app.controller.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.animals.app.service.googleoauth.Google2Api;
import com.animals.app.service.googleoauth.ServiceBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.animals.app.domain.User;
import com.animals.app.domain.UserRole;
import com.animals.app.domain.UserType;
import com.animals.app.repository.UserRepository;
import com.animals.app.repository.Impl.UserRepositoryImpl;

/**
 * Created by 41X on 8/16/2015.
 */
@Path("account")
@PermitAll
public class OAuthAuthorizationResource {

    private static Logger LOG = LogManager.getLogger(OAuthAuthorizationResource.class);

    private final Response NOT_FOUND = Response.status(Response.Status.NOT_FOUND).build();
    private final Response SERVER_ERROR = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

    private UserRepository userRep = new UserRepositoryImpl();

    // Google OAuth preferences
    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
    private static final String PROTECTED_RESOURCE_URL_REFRESH = "https://www.googleapis.com/oauth2/v3/token";
    private static final String SCOPE = "https://mail.google.com/ https://www.googleapis.com/auth/userinfo.email";
    private static final Token EMPTY_TOKEN = null;
    private static final String CALLBACK_URL_PATH_GOOGLE = "webapi/account/login/google_token";
    private static final String GOOGLE_APIKEY = "google.apiKey";
    private static final String GOOGLE_APISECRET = "google.apiSecret";

    // Facebook OAuth preferences
    private static final String PROTECTED_RESOURCE_URL_FB = "https://graph.facebook.com/me";
    private static final String PROTECTED_RESOURCE_URL_FB2 = "https://graph.facebook.com/me?fields=picture.type(large)";
    private static final String CALLBACK_URL_PATH_FACEBOOK = "webapi/account/login/facebook_token";
    private static final String FACEBOOK_APIKEY = "facebook.apiKey";
    private static final String FACEBOOK_APISECRET = "facebook.apiSecret";
    
    // Twitter OAuth preferences
    private static final String PROTECTED_RESOURCE_URL_TW = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String CALL_BACK_URL_PATH_TWITTER = "webapi/account/login/twitter_token";
    private static final String TWITTER_APIKEY = "twitter.consumerKey";
    private static final String TWITTER_APISECRET = "twitter.consumerSecret";
    
    //URL Path
    private static final String LOGIN_GOOGLE_PATH = "webapi/account/login/google";
    private static final String LOGIN_GOOGLE_PATH_TOKEN = "webapi/account/login/google_token";
    private static final String LOGIN_GOOGLE_PATH_DIRECT ="webapi/account/login/google_login_direct";
    private static final String LOGIN_FACEBOOK_PATH = "webapi/account/login/facebook";
    private static final String LOGIN_FACEBOOK_PATH_TOKEN = "webapi/account/login/facebook_token";
    private static final String LOGIN_TWITTER_PATH = "webapi/account/login/twitter";
    private static final String LOGIN_TWITTER_PATH_TOKEN = "webapi/account/login/twitter_token";
    private static final String LOGIN_TWITTER_PATH_DIRECT = "webapi/account/login/twitter_login_direct";
    private static final String URL_TO_SITE_PROFILE = "/#/ua/user/profile";
    private static final String URL_TO_SITE_JOIN_ERROR = "?join=error";    
    
    //parameters to get
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String EMAIL = "email";
    private static final String DATA = "data";
    private static final String URL = "url";
    private static final String ID_TW = "id_str";
    private static final String PICTURE_TW = "profile_image_url";
    private static final String PICTURE_TW_PARAM = "_normal";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String REQUEST_PARAM_1 = "GData-Version";
    private static final String REQUEST_PARAM_2 = "3.0";
    
  //parameters to set
    private static final String NA = "N/A";
    private static final String UNKNOWN = "unknown";
    private static final String GUEST = "guest";
    private static final String TOKEN_ACCESS = "access_token";
    private static final String PROPERTIES_FILE ="project.properties";    
    private static final String CLIENT_ID ="client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String GRANT_TYPE ="grant_type";
    private static final String TOKEN_REFRESH ="refresh_token";
    
    //Messages
    private static final String SUCCESS_MESSAGE_JOIN_GOOGLE = "Successful joining Google account";
    private static final String SUCCESS_REG_MESSAGE = "Successful Registration with GoogleId";
    private static final String SUCCESS_LOG_MESSAGE = "Success login with GoogleId";
    private static final String SUCCESS_LOG_DIR_MESSAGE ="Success direct login with GoogleId";
    private static final String SUCCESS_MESSAGE_JOIN_FACEBOOK ="Successful joining Facebook account";
    private static final String SUCCESS_MESSAGE_LOG_FACEBOOK ="success login with FacebookId";
    private static final String SUCCESS_MESSAGE_REG_FACEBOOK ="Successful Registration with FacebookId";
    private static final String SUCCESS_MESSAGE_JOIN_TWITTER ="Successful joining Twitter account";
    private static final String SUCCESS_MESSAGE_LOG_TWITTER ="success login with Twitter";
    private static final String SUCCESS_MESSAGE_REG_TWITTER ="Successful Registration with Twitter";
    private static final String SUCCESS_MESSAGE_LOG_DIR_TWITTER = "Success direct login with Twitter";
    private static final String ERROR_MESSAGE_PROPERTIES_FILE ="Cannot open and load mail server properties file. Put it on...";
    
    // Session parameters
    private static final String SESSION_USERNAME = "userName";
    private static final String SESSION_USER_ID = "userId";
    private static final String SESSION_USERSURNAME = "userSurname";
    private static final String SESSION_LOGIN = "socialLogin";
    private static final String SESSION_ROLE_ID = "userRoleId";
    private static final String SESSION_USER_ROLE = "userRole";
    private static final String SESSION_SUCCESS = "successMesage";
    private static final String SESSION_ACCESS_TOKEN = "accessToken";
    private static final String SESSION_USER = "user";
    private static final String SESSION_SUCCESS_MESSAGE = "successMesage";
    private static final String REFRESH_GOOGLE_TOKEN = "refreshGoogleToken";
    private static final String TWITTER_TOKEN = "twitterToken";
    private static final String TWITTER_SECRET = "twitterSecret";
    private static final String FACEBOOK_TOKEN = "facebookToken";

    /**
     * OAuth authentication. Google. Phase 1.
     * login into site with GOOGLE Profile
     * Get on Google page to confirm authentication
     * @return authorization URL for further OAuth authentication
     * with token as URL query parameter
     */
    @GET
    @Path("login/google") // http:localhost:8080/webapi/account/login/google
    public Response googleLogin(@Context HttpServletRequest req) {

        // Define URLs and callback
        String callbackUrlG = defineURL(req, LOGIN_GOOGLE_PATH, CALLBACK_URL_PATH_GOOGLE);

        OAuthService service = null;

        try {
            service = new ServiceBuilder()
                    .provider(Google2Api.class)
                    .apiKey(socialsConfig.getProperty(GOOGLE_APIKEY))
                    .apiSecret(socialsConfig.getProperty(GOOGLE_APISECRET))
                    .callback(callbackUrlG)
                    .scope(SCOPE)
                    .offline(true)
                    .build();

        } catch (Exception e) {
            LOG.error(e);
        }

        if (service == null) {
            return NOT_FOUND;
        }

        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

        return Response.status(Response.Status.OK).entity(authorizationUrl).build();
    }

    /**
     * OAuth authentication. Google. Phase 2.
     * Callback from Google resource with token as query parameter
     * Change token to Google Access Token to build request
     * Build request with Access Token to get user data from Google Resource.
     * Get unique GoogleId, userName, user Email, user profile picture from Google.
     * Next could be 3 Cases.
     * Case 1 - Join account with existing one.
     * Case 2 - Login To site with unique GoogleId
     * Case 3 - Registering new user 
     * @return redirect URL to main site
     */
    @GET
    @Path("login/google_token") // http://localhost:8080/webapi/account/login/google_token
    public Response getGoogleAccessToken(@QueryParam("code") String token, @QueryParam("error") String error,
            @Context HttpServletRequest req) {

        // Define URLs and callback
        String callbackUrlG = defineURL(req, LOGIN_GOOGLE_PATH_TOKEN, CALLBACK_URL_PATH_GOOGLE);
        String pathMain = definePathMain(req, LOGIN_GOOGLE_PATH_TOKEN);
        String successURL = defineSuccessUrl(req, LOGIN_GOOGLE_PATH_TOKEN);       

        if (error != null) {        	
            String entryUrl = pathMain + URL_TO_SITE_PROFILE;
            return Response.temporaryRedirect(UriBuilder.fromUri(entryUrl).build()).build();
        }

        Verifier v = new Verifier(token);

        OAuthService service2 = null;
        try {
            service2 = new ServiceBuilder()
                    .provider(Google2Api.class)
                    .apiKey(socialsConfig.getProperty(GOOGLE_APIKEY))
                    .apiSecret(socialsConfig.getProperty(GOOGLE_APISECRET))
                    .callback(callbackUrlG).scope(SCOPE)
                    .offline(true)
                    .build();

        } catch (Exception e) {
            LOG.error(e);
        }

        Token accessToken = new Token(ACCESS_TOKEN, REFRESH_TOKEN);

        accessToken = service2.getAccessToken(EMPTY_TOKEN, v);

        String refreshGoogleToken = accessToken.getSecret();

        // Request protected resource
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service2.signRequest(accessToken, request);
        org.scribe.model.Response response = request.send();

        // JSON string from Google response
        String json = response.getBody();
        
        
        // parse string
        String googleId = null;
        String name = null;
        String link = null;
        String email = null;
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);

            name = (String) jsonObject.get(NAME);
            googleId = (String) jsonObject.get(ID);
            link = (String) jsonObject.get(PICTURE);
            email = (String) jsonObject.get(EMAIL);            

        } catch (ParseException e) {
            LOG.error(e);
        } catch (NullPointerException ex) {
            LOG.error(ex);
        }

        // getting userId from current session
        HttpSession session = req.getSession(true);

        // CASE 1: Editing user profile from MyCabinet. Check if session has
        // parameters
        if (session.getAttribute(SESSION_USER_ID) != null) {
        	
            // Check if user exist by googleId. If exist - we can't join accounts - will be error.
            // ERROR - when login - two accounts with the same GoogleID
            User existUserWithGoogleId = null;
            try {
                existUserWithGoogleId = userRep.getByGoogleId(googleId);
            } catch (Exception e) {
                LOG.error(e);
                return SERVER_ERROR;
            }

            if (existUserWithGoogleId != null) {
                // add params to redirect URL to inform frontend that account is
                // already in use by another user            	
                String errorUrl = successURL + URL_TO_SITE_JOIN_ERROR;
                return Response.temporaryRedirect(UriBuilder.fromUri(errorUrl).build()).build();
            }

            int userId = Integer.parseInt((String) session.getAttribute(SESSION_USER_ID));

            // insert in User value of googleId and picture by userId
            User user = null;
            try {
                user = userRep.getById(userId);
                user.setGoogleId(googleId);
                user.setSocialPhoto(link);
                user.setEmail(email);
                userRep.update(user);

            } catch (Exception e) {
                LOG.error(e);
                return SERVER_ERROR;
            }

            session.setAttribute(SESSION_SUCCESS_MESSAGE, SUCCESS_MESSAGE_JOIN_GOOGLE);
            session.setAttribute(SESSION_USER, user);
            session.setAttribute(REFRESH_GOOGLE_TOKEN, refreshGoogleToken);

            return Response.temporaryRedirect(UriBuilder.fromUri(successURL).build()).build();

        }

        // CASE 2: Login to site. Session is not set. Find User by googleId
        // CASE 3: Registration. Session is not set. Create User with GoogleId

        // Check if user exist by googleId
        User user = null;
        try {
            user = userRep.getByGoogleId(googleId);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        if (user != null) {
            // Case 2
            // creating Session for founded user. Setting params
            setUpSuccessSession(user, session, SUCCESS_LOG_MESSAGE);
            session.setAttribute(REFRESH_GOOGLE_TOKEN, refreshGoogleToken);          

            // Entering to site with Session
            return Response.temporaryRedirect(UriBuilder.fromUri(successURL).build()).build();
        }

        // else CASE 3 - creating User to register
        User userToReg = createEmptyUser(name);

        userToReg.setEmail(email);
        userToReg.setSocialPhoto(link);
        userToReg.setGoogleId(googleId);

        // inserting user to DB
        try {
            userRep.insert(userToReg);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        // creating session
        setUpSuccessSession(userToReg, session, SUCCESS_REG_MESSAGE);
        session.setAttribute(REFRESH_GOOGLE_TOKEN, refreshGoogleToken);

        // Entering to site with Session
        return Response.temporaryRedirect(UriBuilder.fromUri(successURL).build()).build();

    }

    /**
     * Direct OAuth authentication with Google Refresh Token
     * @param google Refresh Token.
     * Change Google refresh Token to Access Token to build request     * 
     * Build request with Access Token to get unique GoogleId from Google Resource.
     * Login To site with unique GoogleId
     * @return redirect URL to main site
     */
    @GET
    @Path("login/google_login_direct") // http://localhost:8080/webapi/account/login/google_login_direct
    public Response directGoogleLoginWithRefreshToken(@Context HttpServletRequest req,
                                                      @QueryParam("code") String refreshGoogleToken) {

        // Define URLs and callback
        String callbackUrlG = defineURL(req, LOGIN_GOOGLE_PATH_DIRECT, CALLBACK_URL_PATH_GOOGLE);
        String successURL = defineSuccessUrl(req, LOGIN_GOOGLE_PATH_DIRECT);

        // Getting new access token with old refreshToken
        OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL_REFRESH);
        request.addBodyParameter(GRANT_TYPE, TOKEN_REFRESH);
        request.addBodyParameter(TOKEN_REFRESH, refreshGoogleToken);
        request.addBodyParameter(CLIENT_ID, socialsConfig.getProperty(GOOGLE_APIKEY));
        request.addBodyParameter(CLIENT_SECRET, socialsConfig.getProperty(GOOGLE_APISECRET));

        org.scribe.model.Response response = request.send();

        // JSON string from Google response
        String json = response.getBody();

        // parse string
        String newAccessToken = null;

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);

            newAccessToken = (String) jsonObject.get(TOKEN_ACCESS);

        } catch (ParseException e) {
            LOG.error(e);
        } catch (NullPointerException ex) {
            LOG.error(ex);
        }

        // New request to protected resource with new accessToken and old
        // refreshToken
        OAuthService service2 = null;
        try {
            service2 = new ServiceBuilder().provider(Google2Api.class)
                    .apiKey(socialsConfig.getProperty(GOOGLE_APIKEY))
                    .apiSecret(socialsConfig.getProperty(GOOGLE_APISECRET))
                    .callback(callbackUrlG)
                    .scope(SCOPE)
                    .build();

        } catch (Exception e) {
            LOG.error(e);
        }

        Token accessToken = new Token(newAccessToken, refreshGoogleToken);

        // Request protected resource
        OAuthRequest request2 = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service2.signRequest(accessToken, request2);
        org.scribe.model.Response response2 = request2.send();

        // JSON string from Google response
        String json2 = response2.getBody();

        // parse string
        String googleId = null;
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json2);

            googleId = (String) jsonObject.get(ID);

        } catch (ParseException e) {
            LOG.error(e);
        } catch (NullPointerException ex) {
            LOG.error(ex);
        }

        // Login to site. Session is not set. Find User by googleId
        HttpSession session = req.getSession(true);

        // Check if user exist by googleId
        User user = null;
        try {
            user = userRep.getByGoogleId(googleId);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        if (user == null) {
            return NOT_FOUND;
        }

        // creating Session for founded user. Setting params
        setUpSuccessSession(user, session, SUCCESS_LOG_DIR_MESSAGE);
        session.setAttribute(REFRESH_GOOGLE_TOKEN, refreshGoogleToken);

        return Response.status(Response.Status.OK).entity(successURL).build();

    }

    
    /**
     * OAuth authentication. Facebook. Phase 1.
     * login into site with Facebook Profile
     * Get on Facebook page to confirm authentication
     * @return authorization URL for further OAuth authentication
     * with token as URL query parameter
     */
    @GET
    @Path("login/facebook") // http:localhost:8080/webapi/account/login/facebook
    public Response facebookLogin(@Context HttpServletRequest req) {

        // Define URLs and callback
        String callbackUrlF = defineURL(req, LOGIN_FACEBOOK_PATH, CALLBACK_URL_PATH_FACEBOOK);

        OAuthService service = null;
        try {
            service = new ServiceBuilder()
                    .provider(FacebookApi.class)
                    .apiKey(socialsConfig.getProperty(FACEBOOK_APIKEY))
                    .apiSecret(socialsConfig.getProperty(FACEBOOK_APISECRET))
                    .callback(callbackUrlF)
                    .build();

        } catch (Exception e) {
            LOG.error(e);
        }

        if (service == null) {
            return Response.status(404).build();
        }

        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);

        return Response.status(Response.Status.OK).entity(authorizationUrl).build();

    }

    
    /**
     * OAuth authentication. Facebook. Phase 2.
     * Callback from Facebook resource with token as query parameter
     * Change token to Facebook Access Token to build request
     * Build request with Access Token to get user data from Facebook Resource.
     * Get unique FacebookId, userName, user profile picture from Facebook.
     * Next could be 3 Cases.
     * Case 1 - Join account with existing one.
     * Case 2 - Login To site with unique FacebookId
     * Case 3 - Registering new user 
     * @return redirect URL to main site
     */
    @GET
    @Path("login/facebook_token") // http://localhost:8080/webapi/account/login/facebook_token
    public Response getFacebookAccessToken(@QueryParam("code") String token, @QueryParam("error") String error,
            @Context HttpServletRequest req) {

        // Define URLs and callback
        String callbackUrlF = defineURL(req, LOGIN_FACEBOOK_PATH_TOKEN, CALLBACK_URL_PATH_FACEBOOK);
        String pathMain = definePathMain(req, LOGIN_FACEBOOK_PATH_TOKEN);
        String successURL = defineSuccessUrl(req, LOGIN_FACEBOOK_PATH_TOKEN);

        if (error != null) {
            String entryUrl = pathMain + URL_TO_SITE_PROFILE;
            return Response.temporaryRedirect(UriBuilder.fromUri(entryUrl).build()).build();
        }

        Verifier v = new Verifier(token);

        OAuthService service = null;
        try {
            service = new ServiceBuilder()
                    .provider(FacebookApi.class)
                    .apiKey(socialsConfig.getProperty(FACEBOOK_APIKEY))
                    .apiSecret(socialsConfig.getProperty(FACEBOOK_APISECRET))
                    .callback(callbackUrlF)
                    .build();

        } catch (Exception e) {
            LOG.error(e);
        }

        Token accessToken = service.getAccessToken(EMPTY_TOKEN, v);

        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL_FB);
        OAuthRequest request2 = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL_FB2);

        service.signRequest(accessToken, request);
        service.signRequest(accessToken, request2);

        request.addHeader(REQUEST_PARAM_1, REQUEST_PARAM_2);
        request2.addHeader(REQUEST_PARAM_1, REQUEST_PARAM_2);

        org.scribe.model.Response response = request.send();
        org.scribe.model.Response response2 = request2.send();

        // JSON string from Facebook response
        String json = response.getBody();
        String json2 = response2.getBody();

        // parse string
        String facebookId = null;
        String name = null;
        String link = null;

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);

            name = (String) jsonObject.get(NAME);
            facebookId = (String) jsonObject.get(ID);

            JSONObject jsonObject2 = (JSONObject) jsonParser.parse(json2);

            JSONObject picture = (JSONObject) jsonObject2.get(PICTURE);
            JSONObject data = (JSONObject) picture.get(DATA);
            link = (String) data.get(URL);

        } catch (ParseException e) {
            LOG.error(e);
        } catch (NullPointerException ex) {
            LOG.error(ex);
        }

        // getting userId from current session
        HttpSession session = req.getSession(true);

        // CASE 1: Editing user profile from MyCabinet. Check if session has
        // parameters
        if (session.getAttribute(SESSION_USER_ID) != null) {

            // Check if user exist by googleId. If exist - we can't join
            // accounts - will be error.
            // ERROR - when login - two accounts with the same GoogleID
            User existUserWithFBId = null;
            try {
                existUserWithFBId = userRep.getByFacebookId(facebookId);
            } catch (Exception e) {
                return SERVER_ERROR;
            }

            if (existUserWithFBId != null) {
                // add params to redirect URL to inform frontend that account is
                // already in use by another user
                String errorUrl = successURL + URL_TO_SITE_JOIN_ERROR;
                return Response.temporaryRedirect(UriBuilder.fromUri(errorUrl).build()).build();
            }

            int userId = Integer.parseInt((String) session.getAttribute(SESSION_USER_ID));

            // insert in User value of facebookId and picture by userId
            User user = null;
            try {
                user = userRep.getById(userId);
                user.setFacebookId(facebookId);
                user.setSocialPhoto(link);
                userRep.update(user);

            } catch (Exception e) {
                return SERVER_ERROR;
            }

            session.setAttribute(SESSION_SUCCESS, SUCCESS_MESSAGE_JOIN_FACEBOOK);
            session.setAttribute(SESSION_USER, user);
            session.setAttribute(FACEBOOK_TOKEN, accessToken.toString());

            return Response.temporaryRedirect(UriBuilder.fromUri(successURL).build()).build();

        }

        // CASE 2: Login to site. Session is not set. Find User by FacebookId
        // CASE 3: Registration. Session is not set. Create User with FacebookId

        // Check if user exist by facebookId
        User user = null;
        try {
            user = userRep.getByFacebookId(facebookId);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        if (user != null) {
            // Case 2
            // creating Session for founded user. Setting params
            setUpSuccessSession(user, session, SUCCESS_MESSAGE_LOG_FACEBOOK);
            session.setAttribute(FACEBOOK_TOKEN, accessToken.toString());

            // Entering to site with Session
            return Response.temporaryRedirect(UriBuilder.fromUri(successURL).build()).build();

        }

        // else CASE 3
        // creating User to register
        User userToReg = createEmptyUser(name);

        userToReg.setFacebookId(facebookId);
        userToReg.setSocialPhoto(link);

        // inserting user to DB
        try {
            userRep.insert(userToReg);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        // creating session
        setUpSuccessSession(userToReg, session, SUCCESS_MESSAGE_REG_FACEBOOK);
        session.setAttribute(SESSION_USER, userToReg);
        session.setAttribute(FACEBOOK_TOKEN, accessToken.toString());

        // Entering to site with Session
        return Response.temporaryRedirect(UriBuilder.fromUri(successURL).build()).build();

    }

    
    /**
     * OAuth authentication. Twitter. Phase 1.
     * login into site with Twitter Profile
     * Get on Twitter page to confirm authentication
     * @return authorization URL for further OAuth authentication
     * with Twitter token & Twitter Secret as URL query parameter
     */
    @GET
    @Path("login/twitter") // http:localhost:8080/webapi/account/login/twitter
    public Response twitterLogin(@Context HttpServletRequest req) {

        // Define URLs and callback
        String callbackUrlTW = defineURL(req, LOGIN_TWITTER_PATH, CALL_BACK_URL_PATH_TWITTER);

        OAuthService service = null;

        try {
            service = new ServiceBuilder()
                    .provider(TwitterApi.class)
                    .apiKey(socialsConfig.getProperty(TWITTER_APIKEY))
                    .apiSecret(socialsConfig.getProperty(TWITTER_APISECRET))
                    .callback(callbackUrlTW)
                    .build();

        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
        }

        if (service == null) {
            return Response.status(404).build();
        }

        Token requestToken = service.getRequestToken();
        String authorizationUrl = service.getAuthorizationUrl(requestToken);

        return Response.status(Response.Status.OK).entity(authorizationUrl).build();

    }

    
    /**
     * OAuth authentication. Twitter. Phase 2.
     * Callback from Twitter resource with Twitter token &
     * Twitter secret as query parameter
     * Change token to Twitter Access Token to build request
     * Build request with Access Token to get user data from Twitter Resource.
     * Get unique TwitterId, userName, user profile picture from Twitter.
     * Next could be 3 Cases.
     * Case 1 - Join account with existing one.
     * Case 2 - Login To site with unique FacebookId
     * Case 3 - Registering new user 
     * @return redirect URL to main site
     */
    @GET
    @Path("login/twitter_token") // http://localhost:8080/webapi/account/login/twitter_token
    public Response getTwitterAccessToken(@QueryParam("oauth_token") String oauth_token,
                                          @QueryParam("oauth_verifier") String oauth_verifier,
                                          @QueryParam("denied") String error,
                                          @Context HttpServletRequest req) {

        // Define URLs and callback
        String callbackUrlTW = defineURL(req, LOGIN_TWITTER_PATH_TOKEN, CALL_BACK_URL_PATH_TWITTER);
        String pathMain = definePathMain(req, LOGIN_TWITTER_PATH_TOKEN);
        String successURL = defineSuccessUrl(req, LOGIN_TWITTER_PATH_TOKEN);

        if (error != null) {
            String entryUrl = pathMain + URL_TO_SITE_PROFILE;
            return Response.temporaryRedirect(UriBuilder.fromUri(entryUrl).build()).build();
        }

        Verifier v = new Verifier(oauth_verifier);
        Token requestToken = new Token(oauth_token, oauth_verifier);

        OAuthService service = null;
        try {
            service = new ServiceBuilder()
                    .provider(TwitterApi.class)
                    .apiKey(socialsConfig.getProperty("twitter.consumerKey"))
                    .apiSecret(socialsConfig.getProperty("twitter.consumerSecret"))
                    .callback(callbackUrlTW)
                    .build();

        } catch (Exception e) {
            LOG.error(e);
        }

        Token accessToken = service.getAccessToken(requestToken, v);

        String tokenTw = accessToken.getToken();
        String secretTw = accessToken.getSecret();

        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL_TW);
        service.signRequest(accessToken, request);
        org.scribe.model.Response response = request.send();

        // JSON string from Google response
        String json = response.getBody();

        // parse string
        String twitterId = null;
        String name = null;
        String link = null;
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);

            name = (String) jsonObject.get(NAME);
            twitterId = (String) jsonObject.get(ID_TW);
            String link2 = (String) jsonObject.get(PICTURE_TW);
            link = link2.replace(PICTURE_TW_PARAM, "");

        } catch (ParseException e) {
            LOG.error(e);
        } catch (NullPointerException ex) {
            LOG.error(ex);
        }

        // getting userId from current session
        HttpSession session = req.getSession(true);

        // CASE 1: Editing user profile from MyCabinet. Check if session has
        // parameters
        if (session.getAttribute(SESSION_USER_ID) != null) {

            // Check if user exist by googleId. If exist - we can't join
            // accounts - will be error.
            // ERROR - when login - two accounts with the same GoogleID
            User existUserWithTWId = null;
            try {
                existUserWithTWId = userRep.getByTwitterId(twitterId);
            } catch (Exception e) {
                LOG.error(e);
                return SERVER_ERROR;
            }

            if (existUserWithTWId != null) {
                // add params to redirect URL to inform frontend that account is
                // already in use by another user
                String errorUrl = successURL + URL_TO_SITE_JOIN_ERROR;
                return Response.temporaryRedirect(UriBuilder.fromUri(errorUrl).build()).build();
            }

            int userId = Integer.parseInt((String) session.getAttribute(SESSION_USER_ID));

            // insert in User value of twitterId and picture by userId
            User user = null;
            try {
                user = userRep.getById(userId);
                user.setTwitterId(twitterId);
                user.setSocialPhoto(link);
                userRep.update(user);

            } catch (Exception e) {
                LOG.error(e);
                return SERVER_ERROR;
            }

            session.setAttribute(SESSION_SUCCESS, SUCCESS_MESSAGE_JOIN_TWITTER);
            session.setAttribute(SESSION_USER, user);
            session.setAttribute(TWITTER_TOKEN, tokenTw);
            session.setAttribute(TWITTER_SECRET, secretTw);

            return Response.temporaryRedirect(UriBuilder.fromUri(successURL).build()).build();

        }

        // CASE 2: Login to site. Session is not set. Find User by FacebookId
        // CASE 3: Registration. Session is not set. Create User with FacebookId

        // Check if user exist by googleId
        User user = null;
        try {
            user = userRep.getByTwitterId(twitterId);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        if (user != null) {
            // Case 2
            // creating Session for founded user. Setting params
            setUpSuccessSession(user, session, SUCCESS_MESSAGE_LOG_TWITTER);
            session.setAttribute(TWITTER_TOKEN, tokenTw);
            session.setAttribute(TWITTER_SECRET, secretTw);

            // Entering to site with Session
            return Response.temporaryRedirect(UriBuilder.fromUri(successURL).build()).build();

        }

        // else CASE 3
        // creating User to register
        User userToReg = createEmptyUser(name);

        userToReg.setTwitterId(twitterId);
        userToReg.setSocialPhoto(link);

        // inserting user to DB
        try {
            userRep.insert(userToReg);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        // creating session
        setUpSuccessSession(userToReg, session, SUCCESS_MESSAGE_REG_TWITTER);
        session.setAttribute("user", userToReg);
        session.setAttribute(TWITTER_SECRET, tokenTw);
        session.setAttribute(TWITTER_SECRET, secretTw);

        // Entering to site with Session
        return Response.temporaryRedirect(UriBuilder.fromUri(successURL).build()).build();

    }

    
    /**
     * Direct OAuth authentication with Twitter Access Token & Twitter Secret
     * @param Twitter Access Token
     * @param Twitter Secret Token.
     * Build request with Access Token & Secret Token 
     * to get unique TwitterId from Twitter Resource.
     * Login To site with unique TwitterId
     * @return redirect URL to main site
     */
    @GET
    @Path("login/twitter_login_direct") // http://localhost:8080/webapi/account/login/twitter_login_direct
    public Response directTwitterLoginWithOldAccessToken(@Context HttpServletRequest req,
                                                         @QueryParam("token") String twitterToken,
                                                         @QueryParam("secret") String twitterSecret) {

        // Define URLs and callback
        String callbackUrlTW = defineURL(req, LOGIN_TWITTER_PATH_DIRECT, CALL_BACK_URL_PATH_TWITTER);
        String pathMain = definePathMain(req, LOGIN_TWITTER_PATH_DIRECT);
        String successURL = defineSuccessUrl(req, LOGIN_TWITTER_PATH_DIRECT);

        Token twitterAccessToken = new Token(twitterToken, twitterSecret);

        OAuthService service = null;
        try {
            service = new ServiceBuilder()
                    .provider(TwitterApi.class)
                    .apiKey(socialsConfig.getProperty(TWITTER_APIKEY))
                    .apiSecret(socialsConfig.getProperty(TWITTER_APISECRET))
                    .callback(callbackUrlTW)
                    .build();

        } catch (Exception e) {
            LOG.error(e);
        }

        // Request protected resource
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL_TW);
        service.signRequest(twitterAccessToken, request);
        org.scribe.model.Response response = request.send();

        // JSON string from Twitter response
        String json = response.getBody();

        // parse string
        String twitterId = null;

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(json);

            twitterId = (String) jsonObject.get(ID_TW);
        } catch (ParseException e) {
            LOG.error(e);
        } catch (NullPointerException ex) {
            LOG.error(ex);
        }

        // Login to site. Session is not set. Find User by googleId
        HttpSession session = req.getSession(true);

        // Check if user exist by twitterId
        User user = null;
        try {
            user = userRep.getByTwitterId(twitterId);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        if (user == null) {
            return NOT_FOUND;
        }

        // creating Session for founded user. Setting params
        setUpSuccessSession(user, session, SUCCESS_MESSAGE_LOG_DIR_TWITTER);
        session.setAttribute(TWITTER_TOKEN, twitterToken);
        session.setAttribute(TWITTER_SECRET, twitterSecret);

        return Response.status(Response.Status.OK).entity(successURL).build();

    }

    private static void setUpSuccessSession(User user, HttpSession session, String success) {

        session.setAttribute(SESSION_USERNAME, user.getName());
        session.setAttribute(SESSION_USER_ID, user.getId().toString());
        session.setAttribute(SESSION_USERSURNAME, user.getSurname());
        session.setAttribute(SESSION_LOGIN, user.getSocialLogin());
        session.setAttribute(SESSION_ROLE_ID, user.getUserRole().get(0).getId().toString());
        session.setAttribute(SESSION_USER_ROLE, user.getUserRole().get(0).getRole());
        session.setAttribute(SESSION_SUCCESS, success);
        session.setAttribute(SESSION_USER, user);

        // creating string for accessToken
        String accessToken = (String) session.getId() + ":" + (String) session.getAttribute(SESSION_USER_ID);

        String accessTokenEncoded = null;

        try {
            byte[] encoded = Base64.encodeBase64(accessToken.getBytes());
            accessTokenEncoded = new String(encoded, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.setAttribute(SESSION_ACCESS_TOKEN, accessTokenEncoded);

    }

    private static String defineURL(HttpServletRequest req, String reqPath, String callback) {

        String pathAll = req.getRequestURL().toString();
        String pathMain = pathAll.replace(reqPath, "");
        String callbackUrl = pathMain + callback;
        return callbackUrl;
    }

    private static String definePathMain(HttpServletRequest req, String string) {
        String pathAll = req.getRequestURL().toString();
        String pathMain = pathAll.replace(string, "");
        return pathMain;
    }

    private static String defineSuccessUrl(HttpServletRequest req, String string) {
        String pathAll = req.getRequestURL().toString();
        String pathMain = pathAll.replace(string, "");
        String SuccessUrl = pathMain + URL_TO_SITE_PROFILE;
        return SuccessUrl;
    }

    private static User createEmptyUser(String userName) {

        User userToReg = new User();

        String userLogin;
        if (userName != null && !userName.isEmpty()) {
            userLogin = userName;
        } else {
            userLogin = UNKNOWN;
        }

        userToReg.setEmail(NA);
        userToReg.setName(userLogin);
        userToReg.setSocialLogin(userLogin);
        userToReg.setSurname(NA);
        userToReg.setActive(true);
        userToReg.setAddress(NA);
        userToReg.setPhone(NA);
        userToReg.setOrganizationInfo(NA);
        userToReg.setOrganizationName(NA);
        userToReg.setPassword(userLogin);

        UserRole userRole = new UserRole();
        userRole.setRole(GUEST);
        userRole.setId(3);
        List<UserRole> list = new ArrayList<UserRole>();
        list.add(userRole);
        userToReg.setUserRole(list);

        UserType userType = new UserType();
        userType.setId(1);
        userToReg.setUserType(userType);

        Date currentDate = new Date(new java.util.Date().getTime());
        userToReg.setRegistrationDate(currentDate);

        return userToReg;

    }

    private static Properties socialsConfig = new Properties();

    {
        fetchConfig();
    }

    /**
     * Open a specific text file containing reCAPTCHA secret key and
     * verification URL.
     */
    private void fetchConfig() {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(PROPERTIES_FILE).getFile());

        try (InputStream input = new FileInputStream(file)) {
            socialsConfig.load(input);
        } catch (IOException ex) {
            LOG.error(ERROR_MESSAGE_PROPERTIES_FILE);
        }
    }

}