package no.entur.nanna.nanna.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import no.entur.nanna.nanna.provider.repository.ProviderRepository;
import org.entur.oauth2.JwtRoleAssignmentExtractor;
import org.entur.oauth2.user.DefaultJwtUserInfoExtractor;
import org.entur.ror.permission.RemoteBabaRoleAssignmentExtractor;
import org.entur.ror.permission.RemoteBabaUserInfoExtractor;
import org.junit.jupiter.api.Test;
import org.rutebanken.helper.organisation.RoleAssignmentExtractor;
import org.rutebanken.helper.organisation.authorization.AuthorizationService;
import org.rutebanken.helper.organisation.authorization.FullAccessAuthorizationService;
import org.rutebanken.helper.organisation.user.UserInfoExtractor;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

class AuthorizationConfigTest {

  private final ApplicationContextRunner contextRunner =
    new ApplicationContextRunner()
      .withUserConfiguration(AuthorizationConfig.class);

  @Test
  void jwtUserInfoExtractor_isCreatedByDefault() {
    contextRunner.run(ctx ->
      assertThat(ctx.getBean(UserInfoExtractor.class))
        .isInstanceOf(DefaultJwtUserInfoExtractor.class)
    );
  }

  @Test
  void jwtRoleAssignmentExtractor_isCreatedByDefault() {
    contextRunner.run(ctx ->
      assertThat(ctx.getBean(RoleAssignmentExtractor.class))
        .isInstanceOf(JwtRoleAssignmentExtractor.class)
    );
  }

  @Test
  void jwtBeansAreCreated_whenExtractorPropertyIsJwt() {
    contextRunner
      .withPropertyValues("nanna.security.role.assignment.extractor=jwt")
      .run(ctx -> {
        assertThat(ctx.getBean(UserInfoExtractor.class))
          .isInstanceOf(DefaultJwtUserInfoExtractor.class);
        assertThat(ctx.getBean(RoleAssignmentExtractor.class))
          .isInstanceOf(JwtRoleAssignmentExtractor.class);
      });
  }

  @Test
  void babaBeansAreCreated_whenExtractorPropertyIsBaba() {
    contextRunner
      .withUserConfiguration(WebClientConfig.class)
      .withPropertyValues(
        "nanna.security.role.assignment.extractor=baba",
        "user.permission.rest.service.url=http://test"
      )
      .run(ctx -> {
        assertThat(ctx.getBean(UserInfoExtractor.class))
          .isInstanceOf(RemoteBabaUserInfoExtractor.class);
        assertThat(ctx.getBean(RoleAssignmentExtractor.class))
          .isInstanceOf(RemoteBabaRoleAssignmentExtractor.class);
      });
  }

  @Test
  void fullAccessAuthorizationService_isCreated_whenPropertyIsFull() {
    contextRunner
      .withPropertyValues("nanna.security.authorization-service=full-access")
      .run(ctx ->
        assertThat(
          ctx.getBean("authorizationService", AuthorizationService.class)
        )
          .isInstanceOf(FullAccessAuthorizationService.class)
      );
  }

  @Test
  void tokenBasedAuthorizationService_isCreated_whenPropertyIsTokenBased() {
    contextRunner
      .withUserConfiguration(ProviderRepositoryConfig.class)
      .withPropertyValues("nanna.security.authorization-service=token-based")
      .run(ctx -> assertThat(ctx.getBean("authorizationService")).isNotNull());
  }

  static class WebClientConfig {

    @Bean
    WebClient webClient() {
      return mock(WebClient.class);
    }
  }

  static class ProviderRepositoryConfig {

    @Bean
    ProviderRepository providerRepository() {
      return mock(ProviderRepository.class);
    }
  }
}
