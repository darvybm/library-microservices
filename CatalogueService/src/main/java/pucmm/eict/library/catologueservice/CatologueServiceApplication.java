package pucmm.eict.library.catologueservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CatologueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatologueServiceApplication.class, args);
	}
}
