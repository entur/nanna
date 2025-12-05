package no.entur.nanna.nanna.enturpartner;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrganisationsCodespacesService {

  private final OrganisationsCodespacesRestClient organisationsCodespacesRestClient;
  private List<OrganisationCodespaces> organisationsCodespaces;

  public OrganisationsCodespacesService(
    OrganisationsCodespacesRestClient organisationsCodespacesRestClient
  ) {
    this.organisationsCodespacesRestClient = organisationsCodespacesRestClient;
  }

  public Double getEnturPartnerOrgIdByCodespace(String codespace) {
    if (organisationsCodespaces == null) {
      organisationsCodespaces =
        organisationsCodespacesRestClient.getOrganisationsCodespaces();
    }
    return organisationsCodespaces
      .stream()
      .filter(v -> v.getCodespaces().contains(codespace))
      .findFirst()
      .map(OrganisationCodespaces::getOrganisationId)
      .orElse(null);
  }
}
