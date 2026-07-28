/*
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *   https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 */

package no.entur.nanna.nanna.security.oauth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import jakarta.servlet.Filter;
import org.entur.oauth2.multiissuer.MultiIssuerAuthenticationManagerResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Drives the production security chain built by {@link NannaWebSecurityConfiguration}.
 * No Spring profile is activated on purpose: the configuration is {@code @Profile("!test")}
 * and would be skipped under the "test" profile used by the rest of the suite.
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(
  classes = {
    NannaWebSecurityConfiguration.class,
    NannaWebSecurityConfigurationTest.SecurityChainDependencies.class,
  }
)
class NannaWebSecurityConfigurationTest {

  // NannaTestApp declares its own @ComponentScan, which drops Spring Boot's TypeExcludeFilter, so a
  // stereotype-annotated static nested class here would be scanned into the Spring Boot contexts of
  // the rest of the suite. Hence @Bean methods without @Configuration (lite mode) below, and a
  // non-static controller, which component scanning skips as not independent.
  static class SecurityChainDependencies {

    @Bean
    MultiIssuerAuthenticationManagerResolver multiIssuerAuthenticationManagerResolver() {
      return Mockito.mock(MultiIssuerAuthenticationManagerResolver.class);
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
      return registrationId -> null;
    }
  }

  @RestController
  class AnyPathController {

    @GetMapping("/**")
    String reached() {
      return "reached";
    }
  }

  @Autowired
  @Qualifier("springSecurityFilterChain")
  private Filter springSecurityFilterChain;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc =
      MockMvcBuilders
        .standaloneSetup(new AnyPathController())
        .addFilters(springSecurityFilterChain)
        .build();
  }

  @ParameterizedTest
  @ValueSource(
    strings = {
      "/services/providers/openapi.json",
      "/actuator/prometheus",
      "/actuator/health",
      "/actuator/health/liveness",
      "/actuator/health/readiness",
    }
  )
  void publicPathIsReachableWithoutAuthentication(String path)
    throws Exception {
    assertEquals(
      200,
      status(path),
      path + " must be permitted without a token"
    );
  }

  // Near misses pin exact-match semantics: nothing here may be widened to /actuator/**.
  @ParameterizedTest
  @ValueSource(
    strings = {
      "/actuator/env",
      "/actuator/health/liveness/extra",
      "/services/providers/openapi.json/extra",
      "/services/providers",
    }
  )
  void otherPathRequiresAuthentication(String path) throws Exception {
    assertEquals(401, status(path), path + " must require a token");
  }

  private int status(String path) throws Exception {
    return mockMvc.perform(get(path)).andReturn().getResponse().getStatus();
  }
}
