package mju.scholarship.member.repository;

import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberGot;
import mju.scholarship.scholoarship.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberGotRepository extends JpaRepository<MemberGot, Long> {

    boolean existsByMemberAndScholarship(Member member, Scholarship scholarship);

    Optional<MemberGot> findByMemberAndScholarship(Member member, Scholarship scholarship);

    List<MemberGot> findByMember(Member member);



}
