package mju.scholarship.scholoarship;

import lombok.Getter;
import mju.scholarship.member.entity.ScholarshipStatus;

public enum ScholarshipProgressStatus {

    ALL,        // 예정
    UPCOMING,   // 진행 중
    ONGOING,    // 종료
    ENDED;

    public static ScholarshipProgressStatus fromValue(String value) {
        try {
            return ScholarshipProgressStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + value);
        }
    }
}
