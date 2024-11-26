package mju.scholarship.member.repository;

import mju.scholarship.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT m FROM Member m ORDER BY m.total DESC")
    List<Member> getRank(Pageable pageable);
}
