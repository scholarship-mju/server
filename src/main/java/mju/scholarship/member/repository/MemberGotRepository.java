package mju.scholarship.member.repository;

import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberGot;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.scholoarship.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberGotRepository extends JpaRepository<MemberGot, Long> {

    boolean existsByMemberAndScholarship(Member member, Scholarship scholarship);

    Optional<MemberGot> findByMemberAndScholarship(Member member, Scholarship scholarship);

    List<MemberGot> findByMember(Member member);

    @Query("SELECT mg.scholarship.id FROM MemberGot mg WHERE mg.member = :member")
    List<Long> findScholarshipIdByMember(Member member);

    @Query("SELECT mg FROM MemberGot mg WHERE (:status IS NULL OR mg.status = :status)")
    List<MemberGot> findAllByStatus(ScholarshipStatus status);

    List<MemberGot> findAllByScholarship(Scholarship scholarship);



}
