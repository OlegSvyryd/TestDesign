package com.animals.app.service;

import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;

import org.eclipse.persistence.jaxb.BeanValidationMode;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.ValidationConfig;
import org.glassfish.jersey.server.validation.internal.InjectingConstraintValidatorFactory;

import com.animals.app.controller.resource.AuthorizationResource;
import com.animals.app.controller.resource.UserResource;
/**
 * ContactCard application configuration
 * JAX-RS application
 */
public class ValidationFilterDomainFields extends ResourceConfig{

    public ValidationFilterDomainFields(){
//        property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
//        property(ServerProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
//        property(ServerProperties.BV_FEATURE_DISABLE, true);
        // Resource Package Address     
        packages("com.animals.app.controller.resource", "com.animals.app.repository.Impl");
        //Validation.
        register(ValidationConfigurationContextResolver.class);
        
        // Providers - JSON.
        register(MoxyJsonFeature.class);
        register(new MoxyJsonConfig().setFormattedOutput(true)
                // Turn off BV otherwise the entities on server would be validated by MOXy as well.
                .property(MarshallerProperties.BEAN_VALIDATION_MODE, BeanValidationMode.NONE)
                .resolver());

    }
    /**
     */
    public static class ValidationConfigurationContextResolver implements ContextResolver<ValidationConfig>{
        @Context
        private ResourceContext resourceContext;
        @Override
        public ValidationConfig getContext(final Class<?> type) {
            return new ValidationConfig().constraintValidatorFactory(resourceContext.getResource(InjectingConstraintValidatorFactory.class));
        }
    }
}