package mju.scholarship.scholoarship.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.ScholarshipProgressStatus;
import mju.scholarship.scholoarship.dto.ScholarshipFilterRequest;

import java.util.List;
import java.util.Map;

import static mju.scholarship.scholoarship.QScholarship.scholarship;

@Slf4j
public class ScholarshipCustomRepositoryImpl implements ScholarshipCustomRepository{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public ScholarshipCustomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public static final Map<String, List<String>> QUALIFICATION_KEYWORDS = Map.of(
            "예체능 특기자", List.of("예·체능", "예술", "체육", "입상", "전국대회", "특기생"),
            "다자녀 가구", List.of("다자녀", "3자녀", "가족관계증명서"),
            "성적우수자", List.of("성적우수", "성적 상위", "학점 우수", "우수한 학생", "우수자"),
            "지역인재", List.of("지역 출신", "지역대학", "○○군", "출신고교", "고등학교 졸업", "관내"),
            "만학도", List.of("만학도", "만 25세 이상", "25세 이상"),
            "봉사활동 우수자", List.of("봉사", "자원봉사", "사회공헌"),
            "리더십/활동 우수자", List.of("학생회", "리더십", "공모전", "대외활동"),
            "다문화가정", List.of("다문화", "다문화가정"),
            "장애인", List.of("장애인", "장애 정도"),
            "국가유공자", List.of("국가유공자", "보훈", "상이군경", "보훈대상")
    );

    @Override
    public List<Scholarship> findAllByFilter(List<String>  qualification, ScholarshipProgressStatus status) {
        return jpaQueryFactory
                .selectFrom(scholarship)
                .where(
                        qualificationFilter(qualification),
                        universityNullFilter(),
                        statusFilter(status)
                ).fetch();
    }



    @Override
    public List<Scholarship> findMyScholarship(Member member) {

        return jpaQueryFactory
                .selectFrom(scholarship)
                .where(
//                        universityEq(member.getUniversity()),
//                        genderEq(member.getGender()),
//                        gradeEq(member.getGrade()),
//                        incomeEq(member.getIncomeQuantile()),
//                        departmentEq(member.getDepartment()),
//                        ageEq(member.getAge()),
//                        addressEq(member.getProvince(), member.getCity())
                )
                .fetch();
    }

    private BooleanExpression qualificationFilter(List<String> qualifications) {

        if (qualifications == null || qualifications.isEmpty()) {
            return null;
        }

        BooleanExpression result = null;

        for (String label : qualifications) {
            List<String> keywords = QUALIFICATION_KEYWORDS.get(label);
            if (keywords == null) continue; // 매핑 안 된 라벨은 무시

            for (String keyword : keywords) {
                BooleanExpression condition = scholarship.specialQualification.containsIgnoreCase(keyword);
                result = (result == null) ? condition : result.or(condition);
            }
        }

        return result;
    }

    private BooleanExpression universityNullFilter() {
        return (scholarship.university.isNull());
    }

    private BooleanExpression statusFilter(ScholarshipProgressStatus status) {
        return (status == null || status == ScholarshipProgressStatus.ALL) ? null : scholarship.progressStatus.eq(status);
    }


//
//    private BooleanExpression nameFilter(String name){
//        if(name == null){
//            return null;
//        }
//    }
//
//        return scholarship.name.contains(name);
//
//    private BooleanExpression universityFilter(String universityCond) {
//        return universityCond != null ? scholarship.university.eq(universityCond) : null;
//    }
//
//    private BooleanExpression genderFilter(String genderCond) {
//        return genderCond != null ? scholarship.gender.eq(genderCond) : null;
//    }
//
//    private BooleanExpression incomeFilter(Integer incomeQuantileCond) {
//        return incomeQuantileCond != null ? scholarship.incomeQuantile.eq(incomeQuantileCond) : null;
//    }
//
//    private BooleanExpression departmentFilter(String departmentCond) {
//        return departmentCond != null ? scholarship.department.eq(departmentCond) : null;
//    }
//
//    private BooleanExpression universityEq(String universityCond) {
//        log.info("universityCond = {}", universityCond);
//        if (universityCond == null) {
//            //장학금에 대학도 null 이면 true
//            return scholarship.university.isNull();
//        }
//        // 장학금의 대학이 null or 두 대학 명이 같을 때 true 반환
//        return scholarship.university.isNull().or(scholarship.university.eq(universityCond));
//    }
//
//    private BooleanExpression genderEq(String genderCond) {
//        //장학금 없으면 무시
//        if(scholarship.gender == null){
//            return null;
//        }
//        // "전체" + 성별이 일치하는 경우
//        return scholarship.gender.eq("전체").or(scholarship.gender.eq(genderCond));
//    }
//
//    private BooleanExpression gradeEq(Double gradeCond) {
//
//        if(gradeCond == null){
//            return scholarship.grade.isNull();
//        }
//
//        // 장학금 최소 학점이 없는 경우 + 최소학점보다 내 학점이 높은 경우
//        return scholarship.grade.isNull().or(scholarship.grade.loe(gradeCond));
//    }
//
//    private BooleanExpression incomeEq(Integer incomeCond) {
//
//        if(incomeCond == null){
//            return scholarship.incomeQuantile.isNull();
//        }
//
//        return scholarship.incomeQuantile.isNull().or(scholarship.incomeQuantile.goe(incomeCond));
//    }
//
//    private BooleanExpression departmentEq(String departmentCond) {
//        if(departmentCond == null){
//            return scholarship.department.isNull();
//        }
//
//        return scholarship.department.isNull().or(scholarship.department.eq(departmentCond));
//    }
//
//    private BooleanExpression ageEq(Integer ageCond) {
//
//        if(ageCond == null){
//            return null;
//        }
//        return scholarship.minAge.loe(ageCond).and(scholarship.maxAge.goe(ageCond));
//    }
//
//    private BooleanExpression addressEq(String provinceCond, String cityCond) {
//        // 1. province와 city가 모두 null인 데이터를 항상 포함
//        BooleanExpression nullCondition = scholarship.province.isNull().and(scholarship.city.isNull());
//
//        // 2. province와 city 조건이 모두 null인 경우
//        if (provinceCond == null && cityCond == null) {
//            return nullCondition;
//        }
//
//        // 3. province와 city가 모두 있는 경우
//        if (provinceCond != null && cityCond != null) {
//            return nullCondition.or(scholarship.province.eq(provinceCond).and(scholarship.city.eq(cityCond)));
//        }
//
//        // 4. province만 있는 경우
//        if (provinceCond != null) {
//            return nullCondition.or(scholarship.province.eq(provinceCond));
//        }
//
//        // 5. city만 있는 경우
//        if (cityCond != null) {
//            return nullCondition.or(scholarship.city.eq(cityCond));
//        }
//
//        // 6. 기본적으로 null 조건을 포함
//        return nullCondition;
//    }


}
