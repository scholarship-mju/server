package mju.scholarship.scholoarship;

import lombok.RequiredArgsConstructor;
import mju.scholarship.scholoarship.dto.CreateScholarshipRequest;
import mju.scholarship.scholoarship.dto.ScholarshipResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("scholarship")
public class ScholarshipController {

    private final ScholarshipService scholarshipService;


    @PostMapping("/create")
    public ResponseEntity<String> createScholarship(@RequestBody CreateScholarshipRequest scholarship) {
        scholarshipService.createScholarship(scholarship);
        return ResponseEntity.ok("Scholarship created");
    }

    @PostMapping("got")
    public ResponseEntity<String> registerGotScholarships(@RequestBody String name) {
        scholarshipService.registerGotScholarships(name);
        return ResponseEntity.ok("Scholarship registered");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Scholarship>> getAllScholarships() {
        return ResponseEntity.ok().body(scholarshipService.getAllScholarships());
    }

//    @GetMapping("my-scholarship")
//    public ResponseEntity<List<Scholarship>> getMyScholarship() {
//        return ResponseEntity.ok().body(scholarshipService.getMyScholarship());
//    }
}
