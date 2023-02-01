package tt.hashtranslator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tt.hashtranslator.model.HashRequest;

public interface HashRequestRepository extends MongoRepository<HashRequest, String> {
}
