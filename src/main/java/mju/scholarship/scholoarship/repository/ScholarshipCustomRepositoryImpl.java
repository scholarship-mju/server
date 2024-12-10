package mju.scholarship.scholoarship.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.member.entity.Member;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.dto.ScholarshipFilterRequest;

import java.util.List;

import static mju.scholarship.scholoarship.QScholarship.scholarship;

@Slf4j
public class ScholarshipCustomRepositoryImpl implements ScholarshipCustomRepository{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    public ScholarshipCustomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Scholarship> findAllByFilter(ScholarshipFilterRequest filter) {
        return jpaQueryFactory
                .selectFrom(scholarship)
                .where(
                        universityFilter(filter.getUniversity()),
                        genderFilter(filter.getGender()),
                        incomeFilter(filter.getIncomeQuantile()),
                        departmentFilter(filter.getDepartment()),
                        ageFilter(filter.getMinAge(), filter.getMaxAge()),
                        nameFilter(filter.getScholarshipName())
                ).fetch();
    }

    @Override
    public List<Scholarship> findMyScholarship(Member member) {

        return jpaQueryFactory
                .selectFrom(scholarship)
                .where(
                        universityEq(member.getUniversity()),
                        genderEq(member.getGender()),
                        gradeEq(member.getGrade()),
                        incomeEq(member.getIncomeQuantile()),
                        departmentEq(member.getDepartment()),
                        ageEq(member.getAge()),
                        addressEq(member.getProvince(), member.getCity())
                )
                .fetch();
    }

    private BooleanExpression nameFilter(String name){
        if(name == null){
            return null;
        }
        return scholarship.name.contains(name);
    }


    private BooleanExpression ageFilter(Integer minAge, Integer maxAge) {
        if (minAge == null && maxAge == null) {
            return null;
        }
        if (minAge != null && maxAge != null) {
            return scholarship.minAge.goe(minAge).and(scholarship.maxAge.loe(maxAge));
        }
        if (minAge != null) {
            return scholarship.minAge.goe(minAge);
        }
        return scholarship.maxAge.loe(maxAge);
    }

    private BooleanExpression universityFilter(String universityCond) {
        return universityCond != null ? scholarship.university.eq(universityCond) : null;
    }

    private BooleanExpression genderFilter(String genderCond) {
        return genderCond != null ? scholarship.gender.eq(genderCond) : null;
    }

    private BooleanExpression incomeFilter(Integer incomeQuantileCond) {
        return incomeQuantileCond != null ? scholarship.incomeQuantile.eq(incomeQuantileCond) : null;
    }

    private BooleanExpression departmentFilter(String departmentCond) {
        return departmentCond != null ? scholarship.department.eq(departmentCond) : null;
    }

    private BooleanExpression universityEq(String universityCond) {
        log.info("universityCond = {}", universityCond);
        if (universityCond == null) {
            //장학금에 대학도 null 이면 true
            return scholarship.university.isNull();
        }
        // 장학금의 대학이 null or 두 대학 명이 같을 때 true 반환
        return scholarship.university.isNull().or(scholarship.university.eq(universityCond));
    }

    private BooleanExpression genderEq(String genderCond) {
        //장학금 없으면 무시
        if(scholarship.gender == null){
            return null;
        }
        // "전체" + 성별이 일치하는 경우
        return scholarship.gender.eq("전체").or(scholarship.gender.eq(genderCond));
    }

    private BooleanExpression gradeEq(Double gradeCond) {

        if(gradeCond == null){
            return scholarship.grade.isNull();
        }

        // 장학금 최소 학점이 없는 경우 + 최소학점보다 내 학점이 높은 경우
        return scholarship.grade.isNull().or(scholarship.grade.loe(gradeCond));
    }

    private BooleanExpression incomeEq(Integer incomeCond) {

        if(incomeCond == null){
            return scholarship.incomeQuantile.isNull();
        }

        return scholarship.incomeQuantile.isNull().or(scholarship.incomeQuantile.goe(incomeCond));
    }

    private BooleanExpression departmentEq(String departmentCond) {
        if(departmentCond == null){
            return scholarship.department.isNull();
        }

        return scholarship.department.isNull().or(scholarship.department.eq(departmentCond));
    }

    private BooleanExpression ageEq(Integer ageCond) {

        if(ageCond == null){
            return null;
        }
        return scholarship.minAge.loe(ageCond).and(scholarship.maxAge.goe(ageCond));
    }

    private BooleanExpression addressEq(String provinceCond, String cityCond) {
        // 1. province와 city가 모두 null인 데이터를 항상 포함
        BooleanExpression nullCondition = scholarship.province.isNull().and(scholarship.city.isNull());

        // 2. province와 city 조건이 모두 null인 경우
        if (provinceCond == null && cityCond == null) {
            return nullCondition;
        }

        // 3. province와 city가 모두 있는 경우
        if (provinceCond != null && cityCond != null) {
            return nullCondition.or(scholarship.province.eq(provinceCond).and(scholarship.city.eq(cityCond)));
        }

        // 4. province만 있는 경우
        if (provinceCond != null) {
            return nullCondition.or(scholarship.province.eq(provinceCond));
        }

        // 5. city만 있는 경우
        if (cityCond != null) {
            return nullCondition.or(scholarship.city.eq(cityCond));
        }

        // 6. 기본적으로 null 조건을 포함
        return nullCondition;
    }


}
