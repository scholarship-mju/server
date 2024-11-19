package mju.scholarship.scholoarship.repository;

import mju.scholarship.member.Member;
import mju.scholarship.scholoarship.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScholarShipRepository extends JpaRepository<Scholarship, Long> ,ScholarshipCustomRepository{

    Optional<Scholarship> findByName(String name);

    @Query("SELECT s FROM Scholarship s " +
            "WHERE s.university = :#{#member.university} " +
            "AND s.age = :#{#member.age} " +
            "AND s.gender = :#{#member.gender} " +
            "AND s.city = :#{#member.city} " +
            "AND s.department = :#{#member.department} " +
            "AND s.grade = :#{#member.grade} " +
            "AND s.incomeQuantile = :#{#member.incomeQuantile}")
    List<Scholarship> findMyScholarships(Member member);


}
