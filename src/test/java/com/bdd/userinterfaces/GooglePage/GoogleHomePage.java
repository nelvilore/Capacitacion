package com.bdd.userinterfaces.GooglePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GoogleMainPage {
    WebDriver driver;
    public GoogleMainPage(WebDriver driver){ this.driver = driver;}

    By inputBusqueda = By.xpath("//*[@class='gLFyf']");



    public WebElement getInputBusqueda(){
        return driver.findElement(inputBusqueda);
    }
}
