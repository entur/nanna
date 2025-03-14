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

package no.entur.nanna.nanna.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsResponseFilter implements ContainerResponseFilter {

  @Override
  public void filter(
    ContainerRequestContext request,
    ContainerResponseContext response
  ) {
    response.getHeaders().add("Access-Control-Allow-Origin", "*");
    response
      .getHeaders()
      .add(
        "Access-Control-Allow-Headers",
        "origin, content-type, accept, authorization"
      );
    response.getHeaders().add("Access-Control-Allow-Credentials", "true");
    response
      .getHeaders()
      .add(
        "Access-Control-Allow-Methods",
        "GET, POST, PUT, DELETE, OPTIONS, HEAD"
      );
  }
}
