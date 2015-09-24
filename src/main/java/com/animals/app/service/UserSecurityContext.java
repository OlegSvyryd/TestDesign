package com.animals.app.service;

import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.animals.app.domain.User;

public class UserSecurityContext implements SecurityContext {
	
	private User user;
	private Principal principal;
	
    @Inject
    javax.inject.Provider<UriInfo> uriInfo;

	public UserSecurityContext(final User user) {
		this.user = user;
        this.principal = new Principal() {

            public String getName() {
                return user.getName();
            }            
            
        };		
	}

	@Override
	public Principal getUserPrincipal() {
		 return this.principal;
	}

	@Override
	public boolean isUserInRole(String role) {			
		String roleUser = user.getUserRole().get(0).getRole();			
		return (role.equals(roleUser));		
	}

	@Override
	public boolean isSecure() {
		 return false;
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

}