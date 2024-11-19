package mju.scholarship.scholoarship.repository;

import mju.scholarship.member.Member;
import mju.scholarship.scholoarship.Scholarship;

import java.util.List;

public interface ScholarshipCustomRepository {

    List<Scholarship> findMyScholarship(Member member);
}
