package no.entur.nanna.nanna.enturpartner;

import java.util.List;

public class OrganisationCodespaces {

  private final Double organisationId;
  private final List<String> codespaces;

  public OrganisationCodespaces(
    Double organisationId,
    List<String> codespaces
  ) {
    this.organisationId = organisationId;
    this.codespaces = codespaces;
  }

  public Double getOrganisationId() {
    return organisationId;
  }

  public List<String> getCodespaces() {
    return codespaces;
  }
}
