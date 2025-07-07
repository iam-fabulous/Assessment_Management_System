package assessment.manager.data.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonProperty;
@Document(collection = "tests")
public class TestEntity {
    @Id
    private String id; // MongoDB generates ObjectId, stored as String

    @JsonProperty("name")
    private String name;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "TestEntity{id='" + id + "', name='" + name + "'}";
    }
}
