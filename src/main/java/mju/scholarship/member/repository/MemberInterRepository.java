package mju.scholarship.member.repository;

import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberInterest;
import mju.scholarship.scholoarship.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberInterRepository extends JpaRepository<MemberInterest, Long> {

    boolean existsByMemberAndScholarship(Member member, Scholarship scholarship);

    Optional<MemberInterest> findByMemberAndScholarship(Member member, Scholarship scholarship);

    List<MemberInterest> findByMember(Member member);



}
