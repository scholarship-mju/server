package mju.scholarship;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
class ScholarshipApplicationTests {

	public static void main(String[] args) {
		SpringApplication.run(ScholarshipApplicationTests.class, args);
	}

}
