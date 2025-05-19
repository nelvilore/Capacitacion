package runners;

import com.bdd.utils.UpdateChromeDriver;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"},
        plugin = {"pretty", "json:target/cucumber-reports/Cucumber.json"},
        tags = "@BusquedaCiudad",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        stepNotifications = true
)
public class RunnerTest {
}