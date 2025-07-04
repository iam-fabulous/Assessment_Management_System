package assessment.manager.services.implementations;


import assessment.manager.data.models.Submission;
import assessment.manager.services.interfaces.SubmissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionServiceImpl  implements SubmissionService {
    @Override
    public Submission submit(Submission submission) {
        return null;
    }

    @Override
    public List<Submission> getByParticipant(String participantId) {
        return List.of();
    }

    @Override
    public Optional<Submission> getById(String id) {
        return Optional.empty();
    }
}
