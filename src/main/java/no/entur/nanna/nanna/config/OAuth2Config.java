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

package no.entur.nanna.nanna.config;

import org.entur.oauth2.AuthorizedWebClientBuilder;
import org.entur.oauth2.multiissuer.MultiIssuerAuthenticationManagerResolver;
import org.entur.oauth2.multiissuer.MultiIssuerAuthenticationManagerResolverBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configure Spring Beans for OAuth2 resource server and OAuth2 client security.
 */
@Configuration
public class OAuth2Config {

  @Bean
  @Profile("!test")
  public MultiIssuerAuthenticationManagerResolver multiIssuerAuthenticationManagerResolver(
    @Value(
      "${nanna.oauth2.resourceserver.auth0.entur.internal.jwt.audience:}"
    ) String enturInternalAuth0Audience,
    @Value(
      "${nanna.oauth2.resourceserver.auth0.entur.internal.jwt.issuer-uri:}"
    ) String enturInternalAuth0Issuer,
    @Value(
      "${nanna.oauth2.resourceserver.auth0.entur.partner.jwt.audience:}"
    ) String enturPartnerAuth0Audience,
    @Value(
      "${nanna.oauth2.resourceserver.auth0.entur.partner.jwt.issuer-uri:}"
    ) String enturPartnerAuth0Issuer,
    @Value(
      "${nanna.oauth2.resourceserver.auth0.ror.jwt.audience:}"
    ) String rorAuth0Audience,
    @Value(
      "${nanna.oauth2.resourceserver.auth0.ror.jwt.issuer-uri:}"
    ) String rorAuth0Issuer,
    @Value(
      "${nanna.oauth2.resourceserver.auth0.ror.claim.namespace:}"
    ) String rorAuth0ClaimNamespace
  ) {
    return new MultiIssuerAuthenticationManagerResolverBuilder()
      .withEnturInternalAuth0Issuer(enturInternalAuth0Issuer)
      .withEnturInternalAuth0Audience(enturInternalAuth0Audience)
      .withEnturPartnerAuth0Issuer(enturPartnerAuth0Issuer)
      .withEnturPartnerAuth0Audience(enturPartnerAuth0Audience)
      .withRorAuth0Issuer(rorAuth0Issuer)
      .withRorAuth0Audience(rorAuth0Audience)
      .withRorAuth0ClaimNamespace(rorAuth0ClaimNamespace)
      .build();
  }

  /**
   * Return a WebClient for authorized API calls.
   * The WebClient inserts a JWT bearer token in the Authorization HTTP header.
   * The JWT token is obtained from the configured Authorization Server.
   *
   * @param properties The spring.security.oauth2.client.registration.* properties
   * @param audience   The API audience, required for obtaining a token from Auth0
   * @return a WebClient for authorized API calls.
   */
  @Profile("!test")
  @Bean
  WebClient webClient(
    WebClient.Builder webClientBuilder,
    OAuth2ClientProperties properties,
    @Value("${nanna.oauth2.client.audience}") String audience
  ) {
    return new AuthorizedWebClientBuilder(webClientBuilder)
      .withOAuth2ClientProperties(properties)
      .withAudience(audience)
      .withClientRegistrationId("nanna")
      .build();
  }
}
