package mju.scholarship.scholoarship.repository;

import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;
import mju.scholarship.scholoarship.dto.ScholarshipFilterRequest;

import java.util.List;

public interface ScholarshipCustomRepository {

    List<Scholarship> findMyScholarship(Member member);

    List<Scholarship> findAllByFilter(String qualification, ScholarshipProgressStatus status);
}
