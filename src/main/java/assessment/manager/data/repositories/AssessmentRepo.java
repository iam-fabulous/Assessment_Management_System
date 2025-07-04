package assessment.manager.data.repositories;

import assessment.manager.data.models.Assessment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepo extends MongoRepository<Assessment, String> {
    List<Assessment> findByCreatorId(String creatorId);
}
