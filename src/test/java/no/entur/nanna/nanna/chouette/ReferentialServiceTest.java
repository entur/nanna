package no.entur.nanna.nanna.chouette;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import no.entur.nanna.nanna.provider.domain.ChouetteInfo;
import no.entur.nanna.nanna.provider.domain.Provider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReferentialServiceTest {

  private final ChouetteReferentialRestClient mockClient = Mockito.mock(
    ChouetteReferentialRestClient.class
  );
  private final ChouetteReferentialService service =
    new ChouetteReferentialService(mockClient);

  @Test
  void testValidateSchemaName() {
    ChouetteReferentialService referentialService =
      new ChouetteReferentialService(null);
    assertTrue(referentialService.validateSchemaName("abc"));
    assertTrue(referentialService.validateSchemaName("rb_abc"));
    assertFalse(referentialService.validateSchemaName("abcd"));
    assertFalse(referentialService.validateSchemaName("rb_abcd"));
    assertFalse(referentialService.validateSchemaName("ab"));
    assertFalse(referentialService.validateSchemaName("rb_ab"));
    assertFalse(referentialService.validateSchemaName(""));
  }

  @Test
  void createChouetteReferential_invalidSchemaName_throwsIllegalArgument() {
    Provider provider = providerWith("abcd", "Provider Name", "org", "user");
    assertThrows(
      IllegalArgumentException.class,
      () -> service.createChouetteReferential(provider)
    );
  }

  @Test
  void createChouetteReferential_nullDataspaceName_throwsIllegalArgument() {
    Provider provider = providerWith("abc", null, "org", "user");
    assertThrows(
      IllegalArgumentException.class,
      () -> service.createChouetteReferential(provider)
    );
  }

  @Test
  void createChouetteReferential_nullOrganisation_throwsIllegalArgument() {
    Provider provider = providerWith("abc", "Provider Name", null, "user");
    assertThrows(
      IllegalArgumentException.class,
      () -> service.createChouetteReferential(provider)
    );
  }

  @Test
  void createChouetteReferential_nullUser_throwsIllegalArgument() {
    Provider provider = providerWith("abc", "Provider Name", "org", null);
    assertThrows(
      IllegalArgumentException.class,
      () -> service.createChouetteReferential(provider)
    );
  }

  @Test
  void createChouetteReferential_validProvider_delegatesToRestClient() {
    Provider provider = providerWith("abc", "Provider Name", "org", "user");
    service.createChouetteReferential(provider);
    verify(mockClient, times(1)).createReferential(any());
  }

  private Provider providerWith(
    String referential,
    String name,
    String organisation,
    String user
  ) {
    return new Provider(
      1L,
      name,
      new ChouetteInfo("xmlns", referential, organisation, user)
    );
  }
}
