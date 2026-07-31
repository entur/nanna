package no.entur.nanna.nanna.chouette;

import static org.junit.jupiter.api.Assertions.*;

import no.entur.nanna.nanna.exceptions.ChouetteServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

class ChouetteReferentialRestClientTest {

  private ChouetteReferentialRestClient clientWith(
    ExchangeFunction exchangeFunction
  ) {
    return new ChouetteReferentialRestClient(
      WebClient.builder().exchangeFunction(exchangeFunction),
      "http://test",
      0
    );
  }

  private static WebClientResponseException responseException(int status) {
    return WebClientResponseException.create(
      status,
      "status " + status,
      HttpHeaders.EMPTY,
      new byte[0],
      null
    );
  }

  // --- is5xx predicate ---

  @Test
  void is5xx_returnsTrueFor5xxException() {
    assertTrue(
      ChouetteReferentialRestClient.is5xx.test(responseException(500))
    );
  }

  @Test
  void is5xx_returnsFalseFor4xxException() {
    assertFalse(
      ChouetteReferentialRestClient.is5xx.test(responseException(404))
    );
  }

  @Test
  void is5xx_returnsFalseForNonWebClientException() {
    assertFalse(
      ChouetteReferentialRestClient.is5xx.test(
        new RuntimeException("network error")
      )
    );
  }

  // --- createReferential error handling ---

  @Test
  void createReferential_conflictResponse_doesNotThrow() {
    ExchangeFunction ef = request -> Mono.error(responseException(409));
    assertDoesNotThrow(() ->
      clientWith(ef).createReferential(new ChouetteReferentialInfo())
    );
  }

  @Test
  void createReferential_nonConflict4xxResponse_throwsChouetteServiceException() {
    ExchangeFunction ef = request -> Mono.error(responseException(400));
    assertThrows(
      ChouetteServiceException.class,
      () -> clientWith(ef).createReferential(new ChouetteReferentialInfo())
    );
  }

  @Test
  void createReferential_networkError_throwsChouetteServiceException() {
    ExchangeFunction ef = request ->
      Mono.error(new RuntimeException("Connection refused"));
    assertThrows(
      ChouetteServiceException.class,
      () -> clientWith(ef).createReferential(new ChouetteReferentialInfo())
    );
  }
}
