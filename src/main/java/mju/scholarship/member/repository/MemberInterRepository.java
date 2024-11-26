package mju.scholarship.member.repository;

import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberInterest;
import mju.scholarship.scholoarship.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberInterRepository extends JpaRepository<MemberInterest, Long> {

    boolean existsByMemberAndScholarship(Member member, Scholarship scholarship);

    Optional<MemberInterest> findByMemberAndScholarship(Member member, Scholarship scholarship);

    List<MemberInterest> findByMember(Member member);

    @Query("SELECT mi.scholarship.id FROM MemberInterest mi WHERE mi.member = :member")
    List<Long> findScholarshipIdByMember(Member member);



}
