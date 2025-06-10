package mju.scholarship.scholoarship.repository;

import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;
import mju.scholarship.scholoarship.dto.ScholarshipFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScholarshipCustomRepository {

    List<Scholarship> findMyScholarship(Member member);

    Page<Scholarship> findAllByFilter(List<String> qualification, ScholarshipProgressStatus status, Pageable pageable);
}
