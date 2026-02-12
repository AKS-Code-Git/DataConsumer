import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:topics.properties")
public class TopicConfig {

    @Value("${app.title}")
    private String appTitle;

    @Value("${app.environment}")
    private String appEnvironment;

    // Getters for the properties
    public String getAppTitle() {
        return appTitle;
    }

    public String getAppEnvironment() {
        return appEnvironment;
    }
}
