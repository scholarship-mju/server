package mju.scholarship.scholoarship.repository;

import mju.scholarship.scholoarship.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScholarShipRepository extends JpaRepository<Scholarship, Long> ,ScholarshipCustomRepository{

    Optional<Scholarship> findByName(String name);

    @Query("SELECT s FROM Scholarship s WHERE s.name LIKE CONCAT('%', :scholarshipName, '%')")
    List<Scholarship> findByScholarshipName(String scholarshipName);
}
