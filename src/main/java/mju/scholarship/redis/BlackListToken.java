package mju.scholarship.redis;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@RedisHash(value = "blackList", timeToLive = 60 * 60 * 24 * 7)
public class BlackListToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Indexed
    private String accessToken;

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public BlackListToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
