package assessment.manager.data.repositories;

import assessment.manager.data.models.Option;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepo extends MongoRepository<Option, String> {
}
