package assessment.manager.data.repositories;

import assessment.manager.data.models.TestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestEntityRepository extends MongoRepository<TestEntity,String> {
}
