package com.github.blokaly.reactiveweather.bdd.stepdefs;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

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

  @Given("^the mock server is reset$")
  public void mock_server_reset() {
    wireMockServer.resetAll();
  }

  @Then("the client receive status code of (\\d+)$")
  public void client_receives_status_code_of(int code) throws Throwable {
    lastResponse.expectStatus().isEqualTo(code);
  }
}