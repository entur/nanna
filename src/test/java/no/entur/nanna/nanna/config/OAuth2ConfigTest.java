package no.entur.nanna.nanna.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class OAuth2ConfigTest {

  private final OAuth2Config config = new OAuth2Config();

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

  @SuppressWarnings("unchecked")
  private List<String> invoke(String audiences) {
    return (List<String>) ReflectionTestUtils.invokeMethod(
      config,
      "parseAudiences",
      (Object) audiences
    );
  }
}
