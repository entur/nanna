package no.entur.nanna.nanna.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.entur.oauth2.multiissuer.MultiIssuerAuthenticationManagerResolver;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class OAuth2ConfigTest {

  private final OAuth2Config config = new OAuth2Config();

  // --- parseAudiences ---

  @Test
  void parseAudiences_null_returnsEmptyList() {
    assertThat(invoke(null)).isEmpty();
  }

  @Test
  void parseAudiences_emptyString_returnsEmptyList() {
    assertThat(invoke("")).isEmpty();
  }

  @Test
  void parseAudiences_whitespaceOnly_returnsEmptyList() {
    assertThat(invoke("   ")).isEmpty();
  }

  @Test
  void parseAudiences_singleAudience_returnsSingletonList() {
    assertThat(invoke("aud-a")).containsExactly("aud-a");
  }

  @Test
  void parseAudiences_commaSeparated_returnsAllAudiences() {
    assertThat(invoke("aud-a,aud-b")).containsExactly("aud-a", "aud-b");
  }

  @Test
  void parseAudiences_threeAudiences_returnsAll() {
    assertThat(invoke("aud-a,aud-b,aud-c"))
      .containsExactly("aud-a", "aud-b", "aud-c");
  }

  // --- multiIssuerAuthenticationManagerResolver ---

  @Test
  void multiIssuerAuthenticationManagerResolver_isCreated_withNoProperties() {
    MultiIssuerAuthenticationManagerResolver resolver =
      config.multiIssuerAuthenticationManagerResolver("", "", "", "");
    assertThat(resolver).isNotNull();
  }

  @Test
  void multiIssuerAuthenticationManagerResolver_isCreated_withMultipleInternalAudiences() {
    MultiIssuerAuthenticationManagerResolver resolver =
      config.multiIssuerAuthenticationManagerResolver(
        "aud-a,aud-b",
        "",
        "",
        ""
      );
    assertThat(resolver).isNotNull();
  }

  @Test
  void multiIssuerAuthenticationManagerResolver_isCreated_withMultiplePartnerAudiences() {
    MultiIssuerAuthenticationManagerResolver resolver =
      config.multiIssuerAuthenticationManagerResolver(
        "",
        "",
        "aud-c,aud-d",
        ""
      );
    assertThat(resolver).isNotNull();
  }

  @SuppressWarnings("unchecked")
  private List<String> invoke(String audiences) {
    return (List<String>) ReflectionTestUtils.invokeMethod(
      config,
      "parseAudiences",
      (Object) audiences
    );
  }
}
