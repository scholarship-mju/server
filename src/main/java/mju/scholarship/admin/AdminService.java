package mju.scholarship.admin;

import lombok.RequiredArgsConstructor;
import mju.scholarship.admin.dto.MemberGotResponse;
import mju.scholarship.member.entity.MemberGot;
import mju.scholarship.member.entity.ScholarshipStatus;
import mju.scholarship.member.repository.MemberGotRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberGotRepository memberGotRepository;

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
}
