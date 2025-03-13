package mju.scholarship.embedding;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class PineconeService {

    @Value("${pinecone.api-key}")
    private String apiKey;
    @Value("${pinecone.index-name}")
    private String indexName;


    private final RestTemplate restTemplate = new RestTemplate();

    public void saveVector(String id, List<Float> vector) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("vectors", List.of(Map.of("id", id, "values", vector)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange("https://" + indexName + ".svc.pinecone.io/vectors/upsert", HttpMethod.POST, entity, String.class);
    }
}
