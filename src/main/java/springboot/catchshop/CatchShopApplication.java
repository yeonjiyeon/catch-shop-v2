package springboot.catchshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CatchShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatchShopApplication.class, args);
	}

}
