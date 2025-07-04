package assessment.manager.data.repositories;

import assessment.manager.data.models.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends MongoRepository<Question, String> {

}
