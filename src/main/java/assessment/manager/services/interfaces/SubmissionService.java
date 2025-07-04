package assessment.manager.services.interfaces;

import assessment.manager.data.models.Submission;

import java.util.List;
import java.util.Optional;

public interface SubmissionService  {
    Submission submit(Submission submission);
    List<Submission> getByParticipant(String participantId);
    Optional<Submission> getById(String id);
}
