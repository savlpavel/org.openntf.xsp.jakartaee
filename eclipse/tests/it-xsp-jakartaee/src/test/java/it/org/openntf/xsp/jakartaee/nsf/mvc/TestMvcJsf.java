package it.org.openntf.xsp.jakartaee.nsf.mvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import it.org.openntf.xsp.jakartaee.AbstractWebClientTest;
import it.org.openntf.xsp.jakartaee.BrowserArgumentsProvider;

@SuppressWarnings("nls")
public class TestMvcJsf extends AbstractWebClientTest {
	
	@ParameterizedTest
	@ArgumentsSource(BrowserArgumentsProvider.class)
	@Order(1)
	public void testHelloPage(WebDriver driver) {
		driver.get(getRestUrl(driver) + "/mvcjsf");

		try {
			String expected = "inputValue" + System.currentTimeMillis();
			{
				WebElement form = driver.findElement(By.xpath("//form[1]"));
	
				WebElement dd = driver.findElement(By.xpath("//dt[text()=\"Request Method\"]/following-sibling::dd[1]"));
				assertEquals("GET", dd.getText());
				
				WebElement input = form.findElement(By.xpath("input[1]"));
				assertTrue(input.getAttribute("id").endsWith(":appGuyProperty"), () -> input.getAttribute("id"));
				input.click();
				// May be set by previous test
				input.clear();
				input.sendKeys(expected);
				assertEquals(expected, input.getAttribute("value"));
				
				WebElement submit = form.findElement(By.xpath("input[@type='submit']"));
				assertEquals("Refresh", submit.getAttribute("value"));
				submit.click();
			}
			{
				
				WebElement form = driver.findElement(By.xpath("//form[1]"));
				
				WebElement span = form.findElement(By.xpath("p/span[1]"));
				assertEquals(expected, span.getText());
			}
		} catch(Exception e) {
			throw new RuntimeException("Encountered exception with page source:\n" + driver.getPageSource(), e);
		}
	}
}
