package mju.scholarship.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import mju.scholarship.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RankResponse {

    public List<Member> memberList = new ArrayList<>();

    public void addRanker(List<Member> memberList){
        this.memberList.addAll(memberList);
    }
}
