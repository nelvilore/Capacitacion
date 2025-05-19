package stepdefinitions;

import com.bdd.questions.ValidarRedireccion;
import com.bdd.tasks.googleSearch.AccederAPrimeraOpcion;
import com.bdd.tasks.googleSearch.EscribirPalabra;
import com.bdd.utils.ConfigHelper;
import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.core.util.EnvironmentVariables;

import java.util.logging.Logger;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class GoogleStepDefinitions {

    private static final Logger LOGGER = Logger.getLogger(GoogleStepDefinitions.class.getName());
    private EnvironmentVariables environmentVariables;

    @Before
    public void prepararEscenario() {

        ConfigHelper.loadAllEnvironmentProperties();
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("Usuario");
        LOGGER.info("Escenario preparado");
    }

    @Dado("^que el actor abre la página de Google$")
    public void AbrirAppGoogle() {

        String googleUrl = ConfigHelper.getProperty("url.google");
        //String googleUrl = environmentVariables.optionalProperty("environments.default.url.google")
                                               //.orElse("https://www.google.com");
        System.out.println("Google URL: " + googleUrl);
        theActorInTheSpotlight().attemptsTo(Open.url(googleUrl));
    }

    @Cuando("^escribo \"([^\"]*)\"$")
    public void escribirTextoEnGoogle(String texto) {
        theActorInTheSpotlight().attemptsTo(
                EscribirPalabra.enGoogle(texto)
        );
    }

    @io.cucumber.java.es.Y("^accedo al primer resultado$")
    public void accederPrimerResultado() {
        theActorInTheSpotlight().attemptsTo(
                AccederAPrimeraOpcion.resultado()
        );
    }

    @io.cucumber.java.es.Entonces("^valido que salí de Google$")
    public void validarQueSalioDeGoogle() {
        theActorInTheSpotlight().should(
                seeThat(ValidarRedireccion.fueraDeGoogle())
        );
    }

}
