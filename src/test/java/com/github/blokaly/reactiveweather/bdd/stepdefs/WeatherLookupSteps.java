package com.github.blokaly.reactiveweather.bdd.stepdefs;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.Assert.assertEquals;

import com.github.blokaly.reactiveweather.data.Weather;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class WeatherLookupSteps extends CucumberSpringContextConfiguration {

  private WebTestClient.ResponseSpec lastResponse;

  @When("^the client calls (\\S+)$")
  public void client_issue_get_health(String endpoint) throws Throwable {
    lastResponse = webTestClient.get().uri(endpoint).exchange();
  }

  @Given("^the weatherstack response of (\\S+) with:$")
  public void mock_weatherstack_response(String endpoint, String json) {
    wireMockServer.stubFor(
        WireMock.get("/weatherstack" + endpoint)
            .willReturn(aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(json))
    );
  }

  @Given("^the openweather response of (\\S+) with:$")
  public void mock_openweather_response(String endpoint, String json) {
    wireMockServer.stubFor(
        WireMock.get("/openweather" + endpoint)
            .willReturn(aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(json))
    );
  }

  @Then("the client receives status code of (\\d+)$")
  public void client_receives_status_code_of(int code) throws Throwable {
    lastResponse.expectStatus().isEqualTo(code);
  }

  @Then("^the client received temperature should be ([\\d\\.]+)$")
  public void client_received_temperature(double temp) {
    var weather = lastResponse.expectBody(Weather.class).returnResult().getResponseBody();
    assertEquals(temp, weather.getTemperature(), 0.001);
  }

  @Given("^the mock server is reset$")
  public void mock_server_reset() {
    wireMockServer.resetAll();
  }

  @Given("^the redis server is flushed$")
  public void redis_reset() { redisFactory.getReactiveConnection().serverCommands().flushAll().then().subscribe();}
}