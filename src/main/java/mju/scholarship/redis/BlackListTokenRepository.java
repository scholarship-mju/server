package mju.scholarship.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListTokenRepository extends CrudRepository<BlackListToken, String> {


    Optional<BlackListToken> findByAccessToken(String accessToken);

}
