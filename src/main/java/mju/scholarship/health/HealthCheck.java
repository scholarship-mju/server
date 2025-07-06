package mju.scholarship.health;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HealthCheck {

    @GetMapping("/")
    public String healthCheck(){
        return "Health Check SUccess";
    }
}
