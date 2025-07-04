package assessment.manager.data.repositories;

import assessment.manager.data.models.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepo extends MongoRepository<Submission, String> {
    List<Submission> findByParticipantId(String participantId);
    List<Submission> findByAssessmentId(String assessmentId);
}
