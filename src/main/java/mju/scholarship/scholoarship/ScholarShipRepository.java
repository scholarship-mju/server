package mju.scholarship.scholoarship;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScholarShipRepository extends JpaRepository<Scholarship, Long> {

    Optional<Scholarship> findByName(String name);


}
