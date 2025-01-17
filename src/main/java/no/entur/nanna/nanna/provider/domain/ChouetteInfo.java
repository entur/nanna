package no.entur.nanna.nanna.provider.domain;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class ChouetteInfo {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "chouette_info_seq"
  )
  @SequenceGenerator(
    name = "chouette_info_seq",
    sequenceName = "chouette_info_seq",
    allocationSize = 1
  )
  public Long id;

  public String xmlns;
  public String xmlnsurl;
  public String referential;
  public String organisation;

  @Column(name = "cuser")
  public String user;

  public String dataFormat;
  public boolean enableValidation;
  public boolean allowCreateMissingStopPlace;
  public boolean enableStopPlaceIdMapping;
  public boolean enableCleanImport;
  public boolean enableAutoImport;
  public boolean enableAutoValidation;
  public boolean enableBlocksExport;
  public boolean generateDatedServiceJourneyIds;
  public boolean googleUpload;

  @Column(name = "google_qa_upload")
  public boolean googleQAUpload;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "CHOUETTE_INFO_SERVICE_LINK_MODES",
    joinColumns = @JoinColumn(name = "CHOUETTE_INFO_ID")
  )
  @Enumerated(EnumType.STRING)
  @Column(name = "TRANSPORT_MODE")
  public Set<TransportMode> generateMissingServiceLinksForModes;

  public Long migrateDataToProvider = null;

  public ChouetteInfo() {}

  public ChouetteInfo(
    String xmlns,
    String referential,
    String organisation,
    String user
  ) {
    this.xmlns = xmlns;
    this.referential = referential;
    this.organisation = organisation;
    this.user = user;
  }

  public ChouetteInfo(
    Long id,
    String prefix,
    String referential,
    String organisation,
    String user
  ) {
    this(prefix, referential, organisation, user);
    this.id = id;
  }

  @Override
  public String toString() {
    return (
      "ChouetteInfo{" +
      "id=" +
      id +
      ", xmlns='" +
      xmlns +
      '\'' +
      ", xmlnsurl='" +
      xmlnsurl +
      '\'' +
      ", referential='" +
      referential +
      '\'' +
      ", organisationDTO='" +
      organisation +
      '\'' +
      ", user='" +
      user +
      '\'' +
      ", enableValidation='" +
      enableValidation +
      '\'' +
      ", allowCreateMissingStopPlace='" +
      allowCreateMissingStopPlace +
      '\'' +
      ", enableStopPlaceIdMapping='" +
      enableStopPlaceIdMapping +
      '\'' +
      ", enableCleanImport='" +
      enableCleanImport +
      '\'' +
      ", enableAutoImport='" +
      enableAutoImport +
      '\'' +
      ", enableAutoValidation='" +
      enableAutoImport +
      '\'' +
      ", enableBlocksExport='" +
      enableBlocksExport +
      '\'' +
      ", generateMissingServiceLinksForModes='" +
      generateMissingServiceLinksForModes +
      '\'' +
      ", generateDatedServiceJourneyIds='" +
      generateDatedServiceJourneyIds +
      '\'' +
      ", googleUpload='" +
      googleUpload +
      '\'' +
      ", googleQAUpload='" +
      googleQAUpload +
      '\'' +
      '}'
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ChouetteInfo that = (ChouetteInfo) o;

    if (!Objects.equals(id, that.id)) {
      return false;
    }
    if (!Objects.equals(xmlns, that.xmlns)) {
      return false;
    }
    if (!Objects.equals(referential, that.referential)) {
      return false;
    }
    if (!Objects.equals(organisation, that.organisation)) {
      return false;
    }
    return Objects.equals(user, that.user);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (xmlns != null ? xmlns.hashCode() : 0);
    result = 31 * result + (referential != null ? referential.hashCode() : 0);
    result = 31 * result + (organisation != null ? organisation.hashCode() : 0);
    result = 31 * result + (user != null ? user.hashCode() : 0);
    return result;
  }
}
