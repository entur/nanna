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

import no.entur.nanna.nanna.provider.repository.ProviderRepository;
import org.entur.oauth2.JwtRoleAssignmentExtractor;
import org.entur.oauth2.user.EnturJwtUserInfoExtractor;
import org.entur.ror.permission.RemoteBabaRoleAssignmentExtractor;
import org.entur.ror.permission.RemoteBabaUserInfoExtractor;
import org.rutebanken.helper.organisation.RoleAssignmentExtractor;
import org.rutebanken.helper.organisation.authorization.AuthorizationService;
import org.rutebanken.helper.organisation.authorization.DefaultAuthorizationService;
import org.rutebanken.helper.organisation.authorization.FullAccessAuthorizationService;
import org.rutebanken.helper.organisation.user.UserInfoExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configure authorization.
 */
@Configuration
public class AuthorizationConfig {

  @ConditionalOnProperty(
    value = "nanna.security.role.assignment.extractor",
    havingValue = "jwt",
    matchIfMissing = true
  )
  @Bean
  public UserInfoExtractor jwtUserInfoExtractor() {
    return new EnturJwtUserInfoExtractor();
  }

  @ConditionalOnProperty(
    value = "nanna.security.role.assignment.extractor",
    havingValue = "baba"
  )
  @Bean
  public UserInfoExtractor babaUserInfoExtractor(
    WebClient webClient,
    @Value("${user.permission.rest.service.url}") String url
  ) {
    return new RemoteBabaUserInfoExtractor(webClient, url);
  }

  @ConditionalOnProperty(
    value = "nanna.security.role.assignment.extractor",
    havingValue = "jwt",
    matchIfMissing = true
  )
  @Bean
  public RoleAssignmentExtractor jwtRoleAssignmentExtractor() {
    return new JwtRoleAssignmentExtractor();
  }

  @ConditionalOnProperty(
    value = "nanna.security.role.assignment.extractor",
    havingValue = "baba"
  )
  @Bean
  public RoleAssignmentExtractor babaRoleAssignmentExtractor(
    WebClient webClient,
    @Value("${user.permission.rest.service.url}") String url
  ) {
    return new RemoteBabaRoleAssignmentExtractor(webClient, url);
  }

  @ConditionalOnProperty(
    value = "nanna.security.authorization-service",
    havingValue = "token-based"
  )
  @Bean("authorizationService")
  public AuthorizationService<Long> tokenBasedAuthorizationService(
    ProviderRepository providerRepository,
    RoleAssignmentExtractor roleAssignmentExtractor
  ) {
    return new DefaultAuthorizationService<>(
      providerId ->
        providerRepository.getProvider(providerId) == null
          ? null
          : providerRepository
            .getProvider(providerId)
            .getChouetteInfo()
            .referential.toUpperCase(),
      roleAssignmentExtractor
    );
  }

  @ConditionalOnProperty(
    value = "nanna.security.authorization-service",
    havingValue = "full-access"
  )
  @Bean("authorizationService")
  public AuthorizationService<Long> fullAccessAuthorizationService() {
    return new FullAccessAuthorizationService();
  }
}
