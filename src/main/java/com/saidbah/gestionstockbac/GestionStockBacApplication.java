package com.saidbah.gestionstockbac;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Gestion stocks" ,description = "Documentation de la gestion des stocks.", version = "v1",license = @License(name = "Licence 1.0")))
public class GestionStockBacApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionStockBacApplication.class, args);
	}

}
