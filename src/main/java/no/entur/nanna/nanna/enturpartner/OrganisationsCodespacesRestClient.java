package no.entur.nanna.nanna.enturpartner;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

@Component
public class OrganisationsCodespacesRestClient {

  private static final int HTTP_TIMEOUT = 10000;

  private final WebClient webClient;
  private final int maxRetryAttempts;

  public OrganisationsCodespacesRestClient(
    @Autowired WebClient.Builder webClientBuilder,
    @Value(
      "${enturpartner.rest.organisations.base.url:}"
    ) String enturpartnerOrganisationsUrl,
    @Value(
      "${enturpartner.rest.organisations.retry.max:3}"
    ) int maxRetryAttempts
  ) {
    this.webClient =
      webClientBuilder
        .baseUrl(enturpartnerOrganisationsUrl)
        .clientConnector(
          new ReactorClientHttpConnector(
            HttpClient
              .create()
              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, HTTP_TIMEOUT)
              .doOnConnected(connection -> {
                connection.addHandlerLast(
                  new ReadTimeoutHandler(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                );
                connection.addHandlerLast(
                  new WriteTimeoutHandler(HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                );
              })
          )
        )
        .build();

    this.maxRetryAttempts = maxRetryAttempts;
  }

  public List<OrganisationCodespaces> getOrganisationsCodespaces() {
    return webClient
      .get()
      .uri("/organisations/codespaces")
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .retrieve()
      .bodyToMono(
        new ParameterizedTypeReference<List<OrganisationCodespaces>>() {}
      )
      .retryWhen(
        Retry.backoff(maxRetryAttempts, Duration.ofSeconds(1)).filter(is5xx)
      )
      .block(Duration.ofMillis(HTTP_TIMEOUT));
  }

  protected static final Predicate<Throwable> is5xx = throwable ->
    throwable instanceof WebClientResponseException webClientResponseException &&
    webClientResponseException.getStatusCode().is5xxServerError();
}
