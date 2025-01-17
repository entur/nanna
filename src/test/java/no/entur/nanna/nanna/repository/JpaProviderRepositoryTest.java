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

package no.entur.nanna.nanna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import no.entur.nanna.nanna.NannaTestApp;
import no.entur.nanna.nanna.provider.domain.ChouetteInfo;
import no.entur.nanna.nanna.provider.domain.Provider;
import no.entur.nanna.nanna.provider.repository.ProviderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = NannaTestApp.class
)
@Transactional
class JpaProviderRepositoryTest {

  @TestConfiguration
  @EnableWebSecurity
  static class TestWebSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
      return http.build();
    }
  }

  @Autowired
  ProviderRepository repository;

  @Test
  void testGetProviders() {
    Collection<Provider> providers = repository.getProviders();
    assertThat(providers).hasSize(3);
  }

  @Test
  void testGetProviderById() {
    Provider provider = repository.getProvider(42L);
    assertThat(provider)
      .isEqualTo(
        new Provider(
          42L,
          "Flybussekspressen",
          new ChouetteInfo(
            1L,
            "flybussekspressen",
            "flybussekspressen",
            "Rutebanken",
            "admin@rutebanken.org"
          )
        )
      );
  }

  @Test
  void testGetProviderByNullLongId() {
    Assertions.assertNull(repository.getProvider((Long) null));
  }

  @Test
  void testGetProviderByNullStringId() {
    Assertions.assertNull(repository.getProvider((String) null));
  }

  @Test
  void testCreateAndUpdateAndDeleteProvider() {
    ChouetteInfo chouetteInfo = new ChouetteInfo(
      null,
      "xmlns",
      "refe",
      "org",
      "user"
    );
    Provider newProvider = new Provider(null, "junit provider", chouetteInfo);
    repository.createProvider(newProvider);

    Provider providerForUpdate = repository.getProvider(newProvider.id);

    repository.updateProvider(providerForUpdate);
    repository.deleteProvider(newProvider.id);

    Provider noProvider = repository.getProvider(newProvider.id);

    Assertions.assertNull(noProvider);
  }
}
