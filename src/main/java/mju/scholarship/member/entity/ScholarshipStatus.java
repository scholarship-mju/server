package mju.scholarship.member.entity;

public enum ScholarshipStatus {

    NOT_VERIFIED(0),   // 인증안됨
    IN_PROGRESS(1),    // 인증중
    VERIFIED(2);       // 인증됨

    private final int value;

    ScholarshipStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ScholarshipStatus fromValue(int value) {
        for (ScholarshipStatus status : ScholarshipStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
