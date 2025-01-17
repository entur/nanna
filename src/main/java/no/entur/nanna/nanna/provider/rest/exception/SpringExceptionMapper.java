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

package no.entur.nanna.nanna.provider.rest.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;

@Provider
public class SpringExceptionMapper
  extends ExceptionMapperBase
  implements ExceptionMapper<NestedRuntimeException> {

  private static final Logger LOGGER = LoggerFactory.getLogger(
    SpringExceptionMapper.class
  );

  @Override
  public Response toResponse(NestedRuntimeException e) {
    LOGGER.debug("Operation failed with exception: {}", e.getMessage(), e);
    Throwable t = e;
    if (e.getRootCause() != null) {
      t = e.getRootCause();
    }

    return super.buildResponse(t);
  }
}
