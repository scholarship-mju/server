package mju.scholarship;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class ScholarshipApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScholarshipApplication.class, args);
	}

	@PostConstruct
	public void setTime() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
