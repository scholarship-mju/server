package mju.scholarship.embedding;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    @Value("${spring.ai.openai.api-key}")
    private final String apiKey;

    private final RestTemplate restTemplate;
    private static final String openaiUrl = "https://api.openai.com/v1/embeddings";


    public List<Float> getEmbedding(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("model", "text-embedding-3-small", "input", List.of(text));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://api.openai.com/v1/embeddings", HttpMethod.POST, entity, Map.class);

        return (List<Float>) ((List<Map<String, Object>>) response.getBody().get("data")).get(0).get("embedding");
    }

}
