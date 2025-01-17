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

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import no.entur.nanna.nanna.filter.CorsResponseFilter;
import no.entur.nanna.nanna.provider.rest.ProviderResource;
import no.entur.nanna.nanna.provider.rest.UserContextResource;
import no.entur.nanna.nanna.provider.rest.exception.*;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig {

  @Bean
  public ServletRegistrationBean<ServletContainer> providersAPIJerseyConfig() {
    ServletRegistrationBean<ServletContainer> publicJersey =
      new ServletRegistrationBean<>(
        new ServletContainer(new ProvidersAPIConfig())
      );
    publicJersey.addUrlMappings("/services/providers/*");
    publicJersey.setName("ProvidersAPI");
    publicJersey.setLoadOnStartup(0);
    publicJersey
      .getInitParameters()
      .put("swagger.scanner.id", "providers-scanner");
    publicJersey
      .getInitParameters()
      .put("swagger.config.id", "providers-swagger-doc");
    return publicJersey;
  }

  private static class ProvidersAPIConfig extends ResourceConfig {

    public ProvidersAPIConfig() {
      register(CorsResponseFilter.class);

      register(ProviderResource.class);
      register(UserContextResource.class);

      register(NotAuthenticatedExceptionMapper.class);
      register(PersistenceExceptionMapper.class);
      register(SpringExceptionMapper.class);
      register(IllegalArgumentExceptionMapper.class);
      register(AccessDeniedExceptionMapper.class);

      register(OpenApiResource.class);
    }
  }
}
