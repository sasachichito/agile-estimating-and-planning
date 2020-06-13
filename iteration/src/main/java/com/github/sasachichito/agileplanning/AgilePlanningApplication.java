package com.github.sasachichito.agileplanning;

import com.google.common.base.Predicate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.*;
import static com.google.common.base.Predicates.*;

@SpringBootApplication
@EnableSwagger2
public class AgilePlanningApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgilePlanningApplication.class, args);
	}

	@Bean
	public Docket agilePlanningApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
					.paths(this.paths())
					.build()
				.useDefaultResponseMessages(false)
				.host("localhost:8081")
				.apiInfo(apiInfo());
	}

	@SuppressWarnings("unchecked")
	private Predicate<String> paths() {
		return or(
				regex("/stories.*"),
				regex("/scope.*"),
				regex("/resources.*"),
				regex("/plans.*"),
				regex("/burns.*"),
				regex("/charts.*"),
				regex("/admin.*"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("agile iteration planning")
				.description("This is a agile iteration planning api.")
				.version("1.0.0")
				.build();
	}
}
