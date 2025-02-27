package mju.scholarship.admin;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mju.scholarship.admin.dto.MemberGotResponse;
import mju.scholarship.member.entity.Member;
import mju.scholarship.member.entity.MemberGot;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.member.repository.MemberGotRepository;
import mju.scholarship.member.repository.MemberRepository;
import mju.scholarship.result.exception.GotScholarshipNotFoundException;
import mju.scholarship.result.exception.MemberNotFoundException;
import mju.scholarship.result.exception.ScholarshipNotFoundException;
import mju.scholarship.scholoarship.Scholarship;
import mju.scholarship.scholoarship.ScholarshipService;
import mju.scholarship.scholoarship.dto.ValidAddScholarshipRequest;
import mju.scholarship.scholoarship.repository.ScholarShipRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberGotRepository memberGotRepository;
    private final MemberRepository memberRepository;
    private final ScholarShipRepository scholarShipRepository;

    public List<MemberGotResponse> gotScholarshipConfirm(ScholarshipStatus status) {

        List<MemberGot> allByStatus = memberGotRepository.findAllByStatus(status);
        List<MemberGotResponse> allByStatusResponse = new ArrayList<>();

        for(MemberGot memberGot : allByStatus){
            MemberGotResponse memberGotResponse = MemberGotResponse.builder()
                    .memberGotId(memberGot.getId())
                    .scholarshipName(memberGot.getScholarship().getName())
                    .scholarshipId(memberGot.getScholarship().getId())
                    .memberName(memberGot.getMember().getNickname())
                    .memberId(memberGot.getMember().getId())
                    .status(memberGot.getStatus())
                    .imageUrl(memberGot.getImageUrl())
                    .build();

            allByStatusResponse.add(memberGotResponse);
        }

        return allByStatusResponse;
    }

    @Transactional
    public void validAddGotScholarship(ValidAddScholarshipRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Scholarship scholarship = scholarShipRepository.findById(request.getScholarshipId())
                .orElseThrow(ScholarshipNotFoundException::new);

        MemberGot memberGot = memberGotRepository.findByMemberAndScholarship(member, scholarship)
                .orElseThrow(GotScholarshipNotFoundException::new);

        memberGot.changeStatus(ScholarshipStatus.VERIFIED);

        member.addTotal();
    }
}
