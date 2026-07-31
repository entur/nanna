package no.entur.nanna.nanna.provider.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import no.entur.nanna.nanna.chouette.ChouetteReferentialService;
import no.entur.nanna.nanna.exceptions.ChouetteServiceException;
import no.entur.nanna.nanna.exceptions.ReferentialAlreadyExistException;
import no.entur.nanna.nanna.provider.domain.ChouetteInfo;
import no.entur.nanna.nanna.provider.domain.Provider;
import no.entur.nanna.nanna.provider.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ProviderResourceTest {

  private final ProviderRepository repository = Mockito.mock(
    ProviderRepository.class
  );
  private final ChouetteReferentialService chouetteService = Mockito.mock(
    ChouetteReferentialService.class
  );
  private final ProviderResource resource = new ProviderResource(
    repository,
    chouetteService
  );

  private static Provider provider(Long id, String referential) {
    return new Provider(
      id,
      "Test Provider",
      new ChouetteInfo("xmlns", referential, "org", "user")
    );
  }

  // --- getProvider ---

  @Test
  void getProvider_unknownId_throwsNotFoundException() {
    when(repository.getProvider(99L)).thenReturn(null);
    assertThrows(NotFoundException.class, () -> resource.getProvider(99L));
  }

  @Test
  void getProvider_knownId_returnsProvider() {
    Provider p = provider(1L, "abc");
    when(repository.getProvider(1L)).thenReturn(p);
    assertThat(resource.getProvider(1L)).isEqualTo(p);
  }

  // --- deleteProvider ---

  @Test
  void deleteProvider_unknownId_throwsNotFoundException() {
    when(repository.getProvider(99L)).thenReturn(null);
    assertThrows(NotFoundException.class, () -> resource.deleteProvider(99L));
  }

  @Test
  void deleteProvider_knownId_deletesChouetteReferentialAndProvider() {
    Provider p = provider(1L, "abc");
    when(repository.getProvider(1L)).thenReturn(p);
    resource.deleteProvider(1L);
    verify(chouetteService).deleteChouetteReferential(p);
    verify(repository).deleteProvider(1L);
  }

  // --- updateProvider ---

  @Test
  void updateProvider_unknownId_throwsNotFoundException() {
    Provider p = provider(99L, "abc");
    when(repository.getProvider(99L)).thenReturn(null);
    assertThrows(NotFoundException.class, () -> resource.updateProvider(p));
  }

  @Test
  void updateProvider_knownId_updatesChouetteReferentialAndProvider() {
    Provider p = provider(1L, "abc");
    when(repository.getProvider(1L)).thenReturn(p);
    resource.updateProvider(p);
    verify(chouetteService).updateChouetteReferential(p);
    verify(repository).updateProvider(p);
  }

  // --- createProvider ---

  @Test
  void createProvider_duplicateReferential_throwsReferentialAlreadyExistException() {
    Provider p = provider(null, "abc");
    when(repository.getProvider("abc")).thenReturn(provider(1L, "abc"));
    assertThrows(
      ReferentialAlreadyExistException.class,
      () -> resource.createProvider(p)
    );
  }

  @Test
  void createProvider_chouetteServiceException_throwsInternalServerErrorException() {
    Provider p = provider(null, "abc");
    when(repository.getProvider("abc")).thenReturn(null);
    doThrow(new ChouetteServiceException("error", new RuntimeException()))
      .when(chouetteService)
      .createChouetteReferential(p);
    assertThrows(
      InternalServerErrorException.class,
      () -> resource.createProvider(p)
    );
  }

  @Test
  void createProvider_validProvider_returnsCreatedProvider() {
    Provider p = provider(null, "abc");
    Provider created = provider(42L, "abc");
    when(repository.getProvider("abc")).thenReturn(null);
    when(repository.createProvider(p)).thenReturn(created);
    assertThat(resource.createProvider(p)).isEqualTo(created);
  }
}
