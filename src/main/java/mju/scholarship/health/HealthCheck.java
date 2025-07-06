package mju.scholarship.health;

import org.springframework.web.bind.annotation.GetMapping;

public class HealthCheck {

    @GetMapping("/")
    public String healthCheck(){
        return "Health Check SUccess";
    }
}
