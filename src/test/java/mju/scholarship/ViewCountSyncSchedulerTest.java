package mju.scholarship;

import mju.scholarship.scholoarship.ScholarshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ViewCountSyncSchedulerTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ScholarshipService scholarshipService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String VIEW_COUNT_KEY = "scholarship:viewCount:";

    @BeforeEach
    void setup() {
        // Redis 데이터 세팅
        for (long i = 1; i <= 100000; i++) {
            String idStr = String.valueOf(i);
            redisTemplate.opsForValue().set(VIEW_COUNT_KEY + idStr, String.valueOf(i * 10));
            redisTemplate.opsForSet().add("dirty_scholarship_ids", idStr);
        }

        // (선택) H2 DB에 dummy row 생성
        for (long i = 1; i <= 100000; i++) {
            jdbcTemplate.update("INSERT INTO scholarship(scholarship_id, view_count) VALUES (?, ?)", i, 0);
        }
    }

    @Test
    void 성능_및_정합성_테스트() {
        long start = System.currentTimeMillis();

        scholarshipService.syncViewCounts();

        long end = System.currentTimeMillis();
        System.out.println("⏱ ViewCount Sync Duration: " + (end - start) + " ms");

        // 정합성 검증 (예: ID = 1의 view_count는 10)
        Integer viewCount = jdbcTemplate.queryForObject(
                "SELECT view_count FROM scholarship WHERE scholarship_id = ?",
                Integer.class,
                1L
        );

        assertEquals(10, viewCount);
    }

    @Test
    void 파이프라인_미적용_속도_측정() {
        long start = System.currentTimeMillis();
        scholarshipService.syncViewCountsWithoutPipeline();
        long end = System.currentTimeMillis();
        System.out.println("🚫 No Pipeline Duration: " + (end - start) + " ms");

        Integer viewCount = jdbcTemplate.queryForObject(
                "SELECT view_count FROM scholarship WHERE scholarship_id = ?",
                Integer.class,
                1L
        );
        assertEquals(10, viewCount);
    }

}
