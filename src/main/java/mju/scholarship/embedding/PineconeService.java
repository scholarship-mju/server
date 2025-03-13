package mju.scholarship.embedding;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PineconeService {

    @Value("${pinecone.api-key}")
    private String apiKey;
    @Value("${pinecone.index-name}")
    private String indexName;

    private final EmbeddingService embeddingService;


    private final RestTemplate restTemplate = new RestTemplate();

    public void saveVector(String id, List<Float> vector) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("vectors", List.of(Map.of("id", id, "values", vector)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange("https://" + indexName + ".svc.pinecone.io/vectors/upsert", HttpMethod.POST, entity, String.class);
    }

    public List<String> searchSimilarScholarships(String userQuery) {
        List<Float> userEmbedding = embeddingService.getEmbedding(userQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "queries", List.of(Map.of("values", userEmbedding)),
                "topK", 5
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://" + indexName + ".svc.pinecone.io/query", HttpMethod.POST, entity, Map.class);

        List<Map<String, Object>> matches = (List<Map<String, Object>>) response.getBody().get("matches");
        return matches.stream().map(match -> (String) match.get("id")).toList();
    }
}
