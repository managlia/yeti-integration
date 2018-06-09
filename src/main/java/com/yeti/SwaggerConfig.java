package com.yeti;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicate;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    

    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
//          .paths(PathSelectors.regex("/yeti/.*"))
          .build();
    }

    private Predicate<RequestHandler> customRequestHandlers() {     
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                Set<RequestMethod> methods = input.supportedMethods();
                return ( methods.contains(RequestMethod.GET) 
                    ||   methods.contains(RequestMethod.DELETE)
                    ||   methods.contains(RequestMethod.PUT)
                    ||   methods.contains(RequestMethod.POST)
                    ||   methods.contains(RequestMethod.PATCH) );
                
            }
        };
    }
    
    
}