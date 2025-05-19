package com.bdd.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.thucydides.core.annotations.Managed;
import org.openqa.selenium.WebDriver;

public class ValidarRedireccion implements Question<Boolean> {

    public static ValidarRedireccion fueraDeGoogle() {
        return new ValidarRedireccion();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        String currentUrl = net.serenitybdd.core.Serenity.getDriver().getCurrentUrl();
        return !currentUrl.contains("google");
    }
}
