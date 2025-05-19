package com.bdd.tasks.googleSearch;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

public class AccederAPrimeraOpcion implements Task {

    public static AccederAPrimeraOpcion resultado() {
        return Tasks.instrumented(AccederAPrimeraOpcion.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(GoogleSearchResultsPage.FIRST_RESULT)
        );
    }
}
