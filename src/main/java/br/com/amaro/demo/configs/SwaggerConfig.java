package br.com.amaro.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger V2 configuration
 * @author Mariane Muniz
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    /**
     * This method allows the application behavior to be described through a YAML file.
     * For this a new resource provider configuration is created.
     * @param defaultResourcesProvider the standard swagger resource provider
     * @return SwaggerResourcesProvider
     */
    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(
            final InMemorySwaggerResourcesProvider defaultResourcesProvider
    ) {
        return () -> {
          final List<SwaggerResource> resources = new ArrayList<>();
          final SwaggerResource wsResource = new SwaggerResource();
          wsResource.setSwaggerVersion("2.9.2");
          wsResource.setLocation("/static/swagger.yaml");
          resources.add(wsResource);
          return resources;
        };
    }

    /**
     * This method adds the static path of the swagger files and also the handler.
     * @param registry the standard Spring resource handler registry
     */
    @Override
    protected void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/*")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}