package mju.scholarship.scholoarship.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import mju.scholarship.member.Member;
import mju.scholarship.scholoarship.Scholarship;

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
            return null;
        }

        // 장학금 최소 학점이 없는 경우 + 최소학점보다 내 학점이 높은 경우
        return scholarship.grade.isNull().or(scholarship.grade.loe(gradeCond));
    }

    private BooleanExpression incomeEq(Integer incomeCond) {

        if(incomeCond == null){
            return null;
        }

        return scholarship.incomeQuantile.isNull().or(scholarship.incomeQuantile.goe(incomeCond));
    }

    private BooleanExpression departmentEq(String departmentCond) {
        if(departmentCond == null){
            return null;
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
        // 1. 하나라도 없으면 null 반환
        if (provinceCond == null || cityCond == null) {
            return null;
        }

        // 2. province와 city가 모두 있을 경우 둘 다 일치해야 함
        if (scholarship.province != null && scholarship.city != null) {
            return scholarship.province.eq(provinceCond).and(scholarship.city.eq(cityCond));
        }

        // 3. province만 있을 경우 province만 일치
        if (scholarship.province != null) {
            return scholarship.province.eq(provinceCond);
        }

        // 4. city만 있을 경우 city만 일치
        if (scholarship.city != null) {
            return scholarship.city.eq(cityCond);
        }

        // 5. province와 city 모두 없을 경우 (실제로는 첫 조건에서 걸러짐)
        return null;
    }


}
