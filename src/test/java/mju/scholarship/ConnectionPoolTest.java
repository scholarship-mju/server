package mju.scholarship;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConnectionPoolTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testMultipleIfStatements() {
        int input = 5; // 조건을 평가할 값

        // 시간 측정 시작
        long startTime = System.nanoTime();

        // 다수의 if문
        if (input == 1) {
            // 아무 작업도 하지 않음
        } else if (input == 2) {
            // 아무 작업도 하지 않음
        } else if (input == 3) {
            // 아무 작업도 하지 않음
        } else if (input == 4) {
            // 아무 작업도 하지 않음
        } else if (input == 5) {
            // 실행되는 조건
            System.out.println("Input is 5");
        } else {
            // 기본 조건
        }

        // 시간 측정 종료
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println("조건 평가에 걸린 시간: " + duration + " ns");
        System.out.println("조건 평가에 걸린 시간: " + duration / 1_000_000.0 + " ms");
    }

    @Test
    void testON2Time() {
        int n = 1000; // n의 크기
        List<Integer> list = new ArrayList<>();

        // 데이터 준비
        for (int i = 0; i < n; i++) {
            list.add(i);
        }

        // O(n²) 작업 측정 시작
        long startTime = System.nanoTime(); // 시작 시간
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                sum += list.get(i) + list.get(j); // 중첩 루프에서 합 계산
            }
        }
        long endTime = System.nanoTime(); // 종료 시간

        long duration = endTime - startTime; // 실행 시간 (나노초 단위)
        System.out.println("O(n²) 작업에 걸린 시간: " + duration + " ns");
        System.out.println("O(n²) 작업에 걸린 시간: " + duration / 1_000_000.0 + " ms");

        // 결과 검증 (단순히 합이 음수가 아닌지 체크)
        assert sum > 0 : "합계가 0 이하입니다!";
    }

    @Test
    void testONTime() {
        int n = 1000; // n의 크기
        List<Integer> list = new ArrayList<>();

        // 데이터 준비
        for (int i = 0; i < n; i++) {
            list.add(i);
        }

        // O(n) 작업 측정 시작
        long startTime = System.nanoTime(); // 시작 시간
        int sum = 0;
        for (int num : list) {
            sum += num; // O(n) 작업: 리스트 순회하며 합계 계산
        }
        long endTime = System.nanoTime(); // 종료 시간

        long duration = endTime - startTime; // 실행 시간 (나노초 단위)
        System.out.println("O(n) 작업에 걸린 시간: " + duration + " ns");
        System.out.println("O(n) 작업에 걸린 시간: " + duration / 1_000_000.0 + " ms");

        // 결과 검증
        int expectedSum = (n - 1) * n / 2; // 0부터 n-1까지의 합 계산
        assert sum == expectedSum : "합계가 예상과 다릅니다!";
    }

    @Test
    void testSingleConnectionTimeNano() throws Exception {
        long startTime = System.nanoTime(); // 시작 시간 (나노초)
        try (Connection connection = dataSource.getConnection()) {
            long endTime = System.nanoTime(); // 끝난 시간 (나노초)
            long duration = endTime - startTime; // 나노초 단위 소요 시간

            System.out.println("커넥션 가져오는 데 걸린 시간: " + duration + " ns");
            System.out.println("커넥션 가져오는 데 걸린 시간: " + duration / 1_000_000.0 + " ms"); // 밀리초로 변환
        }
    }


    @Test
    void testParallelConnectionsNano() throws Exception {
        int parallelTasks = 10;
        long[] connectionTimes = new long[parallelTasks];

        Thread[] threads = new Thread[parallelTasks];
        for (int i = 0; i < parallelTasks; i++) {
            int taskIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    long startTime = System.nanoTime();
                    try (Connection connection = dataSource.getConnection()) {
                        long endTime = System.nanoTime();
                        connectionTimes[taskIndex] = endTime - startTime;
                    }
                } catch (Exception e) {
                    connectionTimes[taskIndex] = -1; // 실패 시 -1 기록
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join(); // 모든 스레드 종료 대기
        }

        // 결과 분석
        long totalTime = 0;
        int successCount = 0;
        for (long time : connectionTimes) {
            if (time > 0) {
                totalTime += time;
                successCount++;
            }
        }

        long averageTime = successCount > 0 ? totalTime / successCount : -1;
        System.out.println("병렬 커넥션 평균 시간 (ns): " + averageTime);
        System.out.println("병렬 커넥션 평균 시간 (ms): " + averageTime / 1_000_000.0);

        assertThat(successCount).isEqualTo(parallelTasks);
    }


    @Test
    void testParallelConnections() throws Exception {
        // 병렬 커넥션 테스트
        int parallelTasks = 10;
        long[] connectionTimes = new long[parallelTasks];

        // 병렬 실행
        Thread[] threads = new Thread[parallelTasks];
        for (int i = 0; i < parallelTasks; i++) {
            int taskIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    try (Connection connection = dataSource.getConnection()) {
                        long endTime = System.currentTimeMillis();
                        connectionTimes[taskIndex] = endTime - startTime;
                    }
                } catch (Exception e) {
                    connectionTimes[taskIndex] = -1; // 실패 시 -1 기록
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        // 결과 출력 및 평균 계산
        long totalTime = 0;
        int successCount = 0;
        for (long time : connectionTimes) {
            if (time > 0) {
                totalTime += time;
                successCount++;
            }
        }

        long averageTime = totalTime / successCount;
        System.out.println("병렬 커넥션 평균 시간: " + averageTime + " ms");

        assertThat(successCount).isEqualTo(parallelTasks); // 모든 작업 성공 확인
        assertThat(averageTime).isLessThan(100); // 평균 시간이 100ms 이하인지 확인
    }
}

