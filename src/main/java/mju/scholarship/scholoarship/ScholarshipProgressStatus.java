package mju.scholarship.scholoarship;

import lombok.Getter;
import mju.scholarship.member.entity.ScholarshipStatus;

@Getter
public enum ScholarshipProgressStatus {

    ALL(0),
    UPCOMING(1),    // 예정
    ONGOING(2),     // 진행 중
    ENDED(3); // 종료

    private final int value;

    ScholarshipProgressStatus(int value) {
        this.value = value;
    }


    public static ScholarshipProgressStatus fromValue(int value) {
        for (ScholarshipProgressStatus status : ScholarshipProgressStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }

}
