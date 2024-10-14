package mju.scholarship.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByPassword(String password);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String username);
}
