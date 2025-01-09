package mju.scholarship.scholoarship.repository;

import mju.scholarship.scholoarship.ScholarshipDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ScholarshipDocumentRepository extends ElasticsearchRepository<ScholarshipDocument, Long> {
}
