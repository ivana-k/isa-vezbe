package rs.ac.uns.ftn.informatika.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import rs.ac.uns.ftn.informatika.config.controller.ProductController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
/*
 * Ukljucivanje Swagger 2
 * http://swagger.io/
 */
@EnableSwagger2
@ComponentScan(basePackageClasses = {
        ProductController.class, 
})
public class SwaggerConfiguration {

	    @Bean
	    public Docket swaggerSpringfoxDocket() {
	        /*
	         * select() metoda vraca instancu ApiSelectorBuilder koja
	         * pruza nacin za kontrolu endpointa koje otkriva Swagger
	         */
	        return new Docket(DocumentationType.SWAGGER_2)
	                .apiInfo(apiInfo())
	                .select()
	                .paths(apiPaths())
	                .build();
	    }
	    
	    @SuppressWarnings("unchecked")
		private Predicate<String> apiPaths() {
	        return Predicates.or(PathSelectors.regex("/api/.*"));
	    }

	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("Swagger 2 API documentation example")
	                .description("The reference documentation for ISA project example.")
	                .termsOfServiceUrl("http://canvas.ftn.uns.ac.rs")
	                .build();
	    }

}
