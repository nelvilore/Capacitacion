package hooks;

import com.bdd.utils.UpdateChromeDriver;
import io.cucumber.java.BeforeAll;

public class DriverHook {

    @BeforeAll
    public static void setupDriver() {
        UpdateChromeDriver.downloadChromeDriver("drivers");
    }
}