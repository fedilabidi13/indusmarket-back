package tn.esprit.usermanagement;

import com.google.maps.GeoApiContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableScheduling
public class UserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}
	@Bean
	public GeoApiContext geoApiContext() {
		return new GeoApiContext.Builder().apiKey("caf83ab54amsh7c09f9cfec15ca6p15df8djsnb130792f6d31").build();}

}
