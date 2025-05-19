package com.bdd.web.page.GooglePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GoogleResultPage {
    WebDriver driver;
    public GoogleResultPage(WebDriver driver) {
        this.driver = driver;
    }

    By h3Resultados = By.xpath("//h3");

    public WebElement getFirstResult(){
        return driver.findElement(h3Resultados);
    }
}
