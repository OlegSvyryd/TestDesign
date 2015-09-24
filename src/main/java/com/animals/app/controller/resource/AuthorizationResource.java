package com.animals.app.controller.resource;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Email;

import com.animals.app.domain.User;
import com.animals.app.repository.Impl.UserRepositoryImpl;
import com.animals.app.service.MailSender;

/**
 * Created by 41X on 8/16/2015.
 */

@Path("account")
@PermitAll
public class AuthorizationResource {

    private static Logger LOG = LogManager.getLogger(AuthorizationResource.class);
    
    private final Response BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build();
    private final Response NOT_FOUND = Response.status(Response.Status.NOT_FOUND).build();
    private final Response SERVER_ERROR = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

    private final int LONG_SESSION = 2592000;
    private final int SHORT_SESSION = 10800;

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
    private static final String SESSION_REMEMBER_ME_ON = "ON";

    private static final String REFRESH_GOOGLE_TOKEN = "refreshGoogleToken";
    private static final String TWITTER_TOKEN = "twitterToken";
    private static final String TWITTER_SECRET = "twitterSecret";
    private static final String FACEBOOK_TOKEN = "facebookToken";

    private static final String REG_PATH = "webapi/account/registration/";
    private static final String RESTORE_PASSWORD_PATH = "webapi/account/restore_password/";
    private static final String PATH_CONFIRM_REG = "#/ua/user/confirmRegistration?username=";
    private static final String PATH_CONFIRM_REG_CODE = "&code=";
    
    private static final String UA_LOCALE = "uk";
    private static final String LOGIN_CONFIRM_REG = "Now confirmation should be done";
    private static final String SUCCESSFULL_LOGIN = "Successful login";
    private static final String SESSION_DESTROYED = "Session destroyed";
    private static final String LOGIN_NOT_UNIQUE = "SocialLogin is already in use by another User";
    private static final String CONFIRMATION = "Now confirmation should be done";
    private static final String SUCCESSFUL_REGISTRATION = "Successful Registration";
    private static final String DESTROYED_SESSION = "Session Destroyed";
    private static final String REG_TEXT_EN_1 = "For registration confirmation on site - ";
    private static final String REG_TEXT_EN_2 = " follow next link - ";
    private static final String REG_TEXT_UA_1 = "Для підтвердження реєстрації на сайті - ";
    private static final String REG_TEXT_UA_2 = " пройдіть за вказаною ссилкою - ";
    private static final String RESTORE_TEXT_EN_1 = "Restore password service on site - ";
    private static final String RESTORE_TEXT_EN_2 = " . User Name - ";
    private static final String RESTORE_TEXT_EN_3 = " . New Password :";
    private static final String RESTORE_TEXT_UA_1 = "Сервіс відновлення паролю на сайті - ";
    private static final String RESTORE_TEXT_UA_2 = " . Ім'я користувача - ";
    private static final String RESTORE_TEXT_UA_3 = " . Новий пароль :"; 
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_BASIC = "Basic";
    private static final String ERROR_RESTORE_1 ="Ther is many users in DB. My Bug";
    private static final String ERROR_RESTORE_2 ="Ther is no user with email in database";
    private static final String ERROR_RESTORE_3 = "User with this email is not active";
    private static final String SUCCESS_RESTORE ="Password Restored succesfully. Send Via mail";

    private UserRepositoryImpl userRep = new UserRepositoryImpl();

    /**
     * login into site
     * @param user credentials in HTTP request header
     * @param rememberMe option
     * @return response with status 200 and parameters for creating session
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("login/{rememberMe}") // http:localhost:8080/webapi/account/login/OFF
    public Response loginToSite(@Context HttpServletRequest req, 
                                @PathParam("rememberMe") @NotNull String rememberMe) {

       
        // reading header from request
        String header = null;
        String sub = null;
        try {
            header = req.getHeader(HEADER_AUTHORIZATION);
            sub = header.replaceFirst(HEADER_BASIC + " ", "");
        } catch (NullPointerException e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        // formating header

        String usernameAndPassword = null;
        String username = null;
        String password = null;
        try {
            byte[] decoded = Base64.decodeBase64(sub);
            usernameAndPassword = new String(decoded, "UTF-8");

            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");

            username = tokenizer.nextToken();
            password = tokenizer.nextToken();

        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        // checking if user exist. If not - return username or password is not
        // correct
        User user;
        try {
            user = userRep.checkIfUserExistInDB(username, password);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        if (user == null)
            return NOT_FOUND;

        if (!user.isActive()) {

            String regWithoutConfirm = buildResponseEntity(0, LOGIN_CONFIRM_REG);

            return Response.status(Response.Status.BAD_REQUEST).entity(regWithoutConfirm).build();
        }

        // creating session
        HttpSession session = req.getSession(true);

        if (rememberMe.equals(SESSION_REMEMBER_ME_ON)) {
            session.setMaxInactiveInterval(LONG_SESSION); // session duration - 30 days
        } else {
            session.setMaxInactiveInterval(SHORT_SESSION); // session duration -90 min
        }

        // setSuccessAtribute(session);
        String sessionSuccess = setUpSuccessSession(user, session, SUCCESSFULL_LOGIN);
        session.setAttribute(SESSION_USER, user);

        return Response.status(Response.Status.OK).entity(sessionSuccess).build();

    }

    /**
     * logout - destroying current session     * 
     * @return response with status 200
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("logout") // http:localhost:8080/webapi/account/logout
    public Response destroySession(@Context HttpServletRequest req) {

        HttpSession session = req.getSession(true);

        // destroying session
        session.invalidate();

        // returning json with empty user
        String destroyedSession = buildResponseEntity(0, SESSION_DESTROYED);

        return Response.status(Response.Status.OK).entity(destroyedSession).build();

    }

    /**
     * Registration of new user 
     * @param user instance to be added to dataBase
     * @param locale for defining language in mail confirmation
     * @return response with status 200. Sending mail to user for registration confirmation
     */
    @POST
    @Path("registration/{locale}") // http:localhost:8080/webapi/account/registration/en
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(@Context HttpServletRequest req, 
                                 @Valid User user,
                                 @PathParam("locale") @NotNull String locale) {

        String socialLogin = user.getSocialLogin();

        // check if user socialLogin unique
        String socialLogin2;
        try {
            socialLogin2 = userRep.checkIfUsernameUnique(socialLogin);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        if (socialLogin2 != null && !socialLogin2.isEmpty()) {

            String socialLoginIsAlreadyInUse = buildResponseEntity(0, LOGIN_NOT_UNIQUE);

            return Response.status(Response.Status.BAD_REQUEST).entity(socialLoginIsAlreadyInUse).build();
        }

        String emailVerificator = UUID.randomUUID().toString();

        user.setEmailVerificator(emailVerificator);

        try {
            userRep.insert(user);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        // sending mail
        String recipientEmail = user.getEmail();

        String username = user.getSocialLogin();

        // Define URLs and callback
        String pathAll = req.getRequestURL().toString();
        String pathMain = pathAll.replace(REG_PATH + locale, "");

        String message = buildResponseMessage(pathMain, username, emailVerificator, locale);

        try {
            MailSender ms = new MailSender();
            ms.newsSend(recipientEmail, message);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        String regWithoutConfirm = buildResponseEntity(1, CONFIRMATION);

        return Response.status(Response.Status.OK).entity(regWithoutConfirm).build();

    }

    /**
     * Registration confirmation of new user 
     * @param user socialLogin
     * @param user verification code
     * @return response with status 200 and parameters for creating session
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("confirmRegistration/{socialLogin}/{code}") // http:localhost:8080/webapi/account/confirmRegistration/socialLogin/code
    public Response loginToSite(@Context HttpServletRequest req, 
                                @PathParam("socialLogin") @NotNull String socialLogin,
                                @PathParam("code") @NotNull String code) {

        // user Verification. checking if user with verification code exist.
        User user;
        try {
            user = userRep.userVerification(socialLogin, code);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        if (user == null)
            return NOT_FOUND;

        // update user active
        user.setActive(true);

        try {
            userRep.update(user);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        // creating session
        HttpSession session = req.getSession(true);

        String sessionSuccessReg = setUpSuccessSession(user, session, SUCCESSFUL_REGISTRATION);
        session.setAttribute(SESSION_USER, user);

        return Response.status(Response.Status.OK).entity(sessionSuccessReg).build();

    }

    /**
     * Refresh session - get session parameters from server side to client (need after OAuth authentication)
     * @return response with status 200 and parameters for creating session
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("refresh") // http:localhost:8080/webapi/account/refresh
    public Response refreshSession(@Context HttpServletRequest req) {

        // creating session
        HttpSession session = req.getSession(true);

        // checking if session is stil going on by geting params from it. if not
        // - returning json with empty user
        if (session.getAttribute(SESSION_USER_ID) == null) {

            String destroyedSession = buildResponseEntity(0, DESTROYED_SESSION);

            return Response.status(Response.Status.BAD_REQUEST).entity(destroyedSession).build();

        }

        // if session has params - returning json with session same params.
        // REFRESH
        String str = buildResponse(session);

        return Response.status(Response.Status.OK).entity(str).build();

    }    

    
    /**
     * restore Password via User Email 
     * @param email for look up
     * @param locale for defining language in mail confirmation
     * @return response with status 200
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("restore_password/{email}/{locale}") // http:localhost:8080/webapi/account/restore_password/email/uk
    public Response restorePassword(@Context HttpServletRequest req,
                                    @PathParam("email") @Email String email,
                                    @PathParam("locale") @NotNull String locale) {
               
        // check if user exist with email
        User user;
        try {
            user = userRep.findUserByEmail(email);            
        } catch (Exception e) {
            LOG.error(e);
            String aLotOfUsers = buildResponseEntity(-1, ERROR_RESTORE_1);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(aLotOfUsers).build(); 
        }
        
        if (user == null){
            
            String userWithEmailNotFound = buildResponseEntity(0, ERROR_RESTORE_2);
            return Response.status(Response.Status.NOT_FOUND).entity(userWithEmailNotFound).build(); 
        }
        
        if (!user.isActive()){
            
            String userIsNotActive = buildResponseEntity(-2, ERROR_RESTORE_3);
            return Response.status(Response.Status.BAD_REQUEST).entity(userIsNotActive).build();
            
        }

        String newPassword = RandomStringUtils.random(8, true, true);
        String newPasswordHasshed = getMd5(newPassword);     

        user.setPassword(newPasswordHasshed);

        try {
            userRep.update(user);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        // sending mail
        String recipientEmail = user.getEmail();

        String username = user.getSocialLogin();

        // Define URLs and callback
        String pathAll = req.getRequestURL().toString();
        String pathMain = pathAll.replace(RESTORE_PASSWORD_PATH + email + "/" + locale, "");

        String message = buildRestorePasswordMessage(pathMain, username, newPassword, locale);

        try {
            MailSender ms = new MailSender();
            ms.newsSend(recipientEmail, message);
        } catch (Exception e) {
            LOG.error(e);
            return SERVER_ERROR;
        }

        String restorePasswordConfirm = buildResponseEntity(1, SUCCESS_RESTORE);

        return Response.status(Response.Status.OK).entity(restorePasswordConfirm).build();

    }
    

    private static String setUpSuccessSession(User user, HttpSession session, String success) {

        session.setAttribute(SESSION_USERNAME, user.getName());
        session.setAttribute(SESSION_USER_ID, user.getId().toString());
        session.setAttribute(SESSION_USERSURNAME, user.getSurname());
        session.setAttribute(SESSION_LOGIN, user.getSocialLogin());
        session.setAttribute(SESSION_ROLE_ID, user.getUserRole().get(0).getId().toString());
        session.setAttribute(SESSION_USER_ROLE, user.getUserRole().get(0).getRole());
        session.setAttribute(SESSION_SUCCESS, success);

        // creating string for accessToken
        String accessToken = (String) session.getId() + ":" + (String) session.getAttribute(SESSION_USER_ID);

        String accessTokenEncoded = null;

        try {
            byte[] encoded = Base64.encodeBase64(accessToken.getBytes());
            accessTokenEncoded = new String(encoded, "UTF-8");
        } catch (IOException e) {
            LOG.error(e);
            e.printStackTrace();
        }

        session.setAttribute(SESSION_ACCESS_TOKEN, accessTokenEncoded);

        // creating JSON string with session params
        String str = "{\"sessionId\" : \"" + (String) session.getId() + 
                      "\", \"userId\" : \"" + (String) session.getAttribute(SESSION_USER_ID) + 
                      "\", \"userName\" : \"" + (String) session.getAttribute(SESSION_USERNAME) + 
                      "\", \"userSurname\" : \"" + (String) session.getAttribute(SESSION_USERSURNAME) +
                      "\", \"socialLogin\" : \"" + (String) session.getAttribute(SESSION_LOGIN) + 
                      "\", \"userRole\" : \"" + (String) session.getAttribute(SESSION_USER_ROLE) + 
                      "\", \"userRoleId\" : \"" + (String) session.getAttribute(SESSION_ROLE_ID) + 
                      "\", \"successMesage\" : \"" + (String) session.getAttribute(SESSION_SUCCESS) + 
                      "\", \"accessToken\" : \"" + (String) session.getAttribute(SESSION_ACCESS_TOKEN) + "\"}";
        return str;
    };

    private static String buildResponse(HttpSession session) {

        String str = "{\"sessionId\" : \"" + (String) session.getId() + 
                      "\", \"userId\" : \"" + (String) session.getAttribute(SESSION_USER_ID) + 
                      "\", \"userName\" : \"" + (String) session.getAttribute(SESSION_USERNAME) + 
                      "\", \"userSurname\" : \"" + (String) session.getAttribute(SESSION_USERSURNAME) + 
                      "\", \"socialLogin\" : \"" + (String) session.getAttribute(SESSION_LOGIN) + 
                      "\", \"userRole\" : \"" + (String) session.getAttribute(SESSION_USER_ROLE) + 
                      "\", \"userRoleId\" : \"" + (String) session.getAttribute(SESSION_ROLE_ID) + 
                      "\", \"successMesage\" : \"" + (String) session.getAttribute(SESSION_SUCCESS) + 
                      "\", \"accessToken\" : \"" + (String) session.getAttribute(SESSION_ACCESS_TOKEN) + 
                      "\", \"refreshGoogleToken\" : \"" + (String) session.getAttribute(REFRESH_GOOGLE_TOKEN) +
                      "\", \"twitterToken\" : \"" + (String) session.getAttribute(TWITTER_TOKEN) + 
                      "\", \"twitterSecret\" : \"" + (String) session.getAttribute(TWITTER_SECRET) + 
                      "\", \"facebookToken\" : \"" + (String) session.getAttribute(FACEBOOK_TOKEN) + "\"}";

        return str;
    }

    private String buildResponseEntity(int i, String message) {

        String entity = "{\"userId\" : " + i + ", \"message\" : \"" + message + "\"}";

        return entity;
    }

    private String buildResponseMessage(String pathMain, String username, String emailVerificator, String locale) {

        String message;

        if (locale.equals(UA_LOCALE)) {
            message = REG_TEXT_UA_1 + pathMain + REG_TEXT_UA_2 + pathMain + PATH_CONFIRM_REG + username
                    + PATH_CONFIRM_REG_CODE + emailVerificator;
        } else {
            message = REG_TEXT_EN_1 + pathMain + REG_TEXT_EN_2 + pathMain + PATH_CONFIRM_REG + username
                    + PATH_CONFIRM_REG_CODE + emailVerificator;
        }

        return message;
    }
    
    private String buildRestorePasswordMessage(String pathMain, String username, String newPassword, String locale) {

        String message;

        if (locale.equals(UA_LOCALE)) {
            message = RESTORE_TEXT_UA_1 + pathMain + RESTORE_TEXT_UA_2 + username + RESTORE_TEXT_UA_3 + newPassword;
        } else {
        	message = RESTORE_TEXT_EN_1 + pathMain + RESTORE_TEXT_EN_2 + username + RESTORE_TEXT_EN_3 + newPassword;
        }

        return message;
    }
    
    private static String getMd5(String md5) {
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