package mju.scholarship.scholoarship;

import lombok.Getter;
import mju.scholarship.member.entity.ScholarshipStatus;

public enum ScholarshipProgressStatus {

    UPCOMING,   // 예정
    ONGOING,    // 진행중
    ENDED; //종료

    public static ScholarshipProgressStatus fromValue(String value) {
        try {
            return ScholarshipProgressStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + value);
        }
    }
}
