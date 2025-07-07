package assessment.manager.controllers;

import assessment.manager.data.models.TestEntity;
import assessment.manager.data.repositories.TestEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/assessments")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestEntityRepository repository;

    @GetMapping("/test")
    public String test() {
        return "MongoDB test endpoint is working!";
    }

    @PostMapping("/test")
    public ResponseEntity<?> create(@RequestBody TestEntity entity) {
        try {
            logger.info("Received entity: {}", entity);
            TestEntity savedEntity = repository.save(entity);
            logger.info("Saved entity: {}", savedEntity);
            return ResponseEntity.ok(savedEntity);
        } catch (Exception e) {
            logger.error("Error saving entity", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save entity: " + e.getMessage());
        }
    }
}