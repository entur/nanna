package no.entur.nanna.nanna.security.oauth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.entur.oauth2.multiissuer.MultiIssuerAuthenticationManagerResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@WebMvcTest
@Import(NannaWebSecurityConfiguration.class)
@ActiveProfiles("security-test")
class NannaWebSecurityConfigurationTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  MultiIssuerAuthenticationManagerResolver multiIssuerAuthenticationManagerResolver;

  @MockitoBean
  ClientRegistrationRepository clientRegistrationRepository;

  @Autowired
  CorsConfigurationSource corsConfigurationSource;

  @Test
  void corsConfiguration_allowsAllOrigins() {
    var request = new MockHttpServletRequest("GET", "/api");
    CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
      request
    );
    assertThat(config.getAllowedOrigins()).containsExactly("*");
  }

  @Test
  void corsConfiguration_allowsRequiredHeaders() {
    var request = new MockHttpServletRequest("GET", "/api");
    CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
      request
    );
    assertThat(config.getAllowedHeaders())
      .containsExactlyInAnyOrder(
        "Origin",
        "Accept",
        "X-Requested-With",
        "Content-Type",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers",
        "Authorization",
        "x-correlation-id",
        "Et-Client-Name",
        "baggage",
        "sentry-trace"
      );
  }

  @Test
  void corsConfiguration_allowsRequiredMethods() {
    var request = new MockHttpServletRequest("GET", "/api");
    CorsConfiguration config = corsConfigurationSource.getCorsConfiguration(
      request
    );
    assertThat(config.getAllowedMethods())
      .containsExactlyInAnyOrder("GET", "PUT", "POST", "DELETE");
  }

  @Test
  void openApiEndpoint_isPermittedWithoutAuthentication() throws Exception {
    mockMvc
      .perform(get("/services/providers/openapi.json"))
      .andExpect(result ->
        assertThat(result.getResponse().getStatus())
          .isNotEqualTo(HttpStatus.UNAUTHORIZED.value())
      );
  }

  @Test
  void actuatorPrometheus_isPermittedWithoutAuthentication() throws Exception {
    mockMvc
      .perform(get("/actuator/prometheus"))
      .andExpect(result ->
        assertThat(result.getResponse().getStatus())
          .isNotEqualTo(HttpStatus.UNAUTHORIZED.value())
      );
  }

  @Test
  void actuatorHealth_isPermittedWithoutAuthentication() throws Exception {
    mockMvc
      .perform(get("/actuator/health"))
      .andExpect(result ->
        assertThat(result.getResponse().getStatus())
          .isNotEqualTo(HttpStatus.UNAUTHORIZED.value())
      );
  }

  @Test
  void actuatorHealthLiveness_isPermittedWithoutAuthentication()
    throws Exception {
    mockMvc
      .perform(get("/actuator/health/liveness"))
      .andExpect(result ->
        assertThat(result.getResponse().getStatus())
          .isNotEqualTo(HttpStatus.UNAUTHORIZED.value())
      );
  }

  @Test
  void actuatorHealthReadiness_isPermittedWithoutAuthentication()
    throws Exception {
    mockMvc
      .perform(get("/actuator/health/readiness"))
      .andExpect(result ->
        assertThat(result.getResponse().getStatus())
          .isNotEqualTo(HttpStatus.UNAUTHORIZED.value())
      );
  }

  @Test
  void anyOtherEndpoint_requiresAuthentication() throws Exception {
    mockMvc
      .perform(get("/services/providers"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void csrfIsDisabled_postWithoutCsrfTokenReturnsUnauthorizedNotForbidden()
    throws Exception {
    // If CSRF were enabled, an unauthenticated POST without a CSRF token would return 403.
    // Returning 401 proves the CSRF filter is not in the chain.
    mockMvc
      .perform(post("/services/providers"))
      .andExpect(status().isUnauthorized());
  }
}
