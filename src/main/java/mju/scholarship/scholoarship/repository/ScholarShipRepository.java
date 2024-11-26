package mju.scholarship.scholoarship.repository;

import mju.scholarship.scholoarship.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScholarShipRepository extends JpaRepository<Scholarship, Long> ,ScholarshipCustomRepository{

    Optional<Scholarship> findByName(String name);

}
