package no.entur.nanna.nanna.provider.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ChouetteInfoTest {

  @Test
  void constructor4Args_setsFieldsCorrectly() {
    ChouetteInfo info = new ChouetteInfo("rb_nsr", "ref-abc", "Org", "admin");
    assertThat(info.xmlns).isEqualTo("rb_nsr");
    assertThat(info.referential).isEqualTo("ref-abc");
    assertThat(info.organisation).isEqualTo("Org");
    assertThat(info.user).isEqualTo("admin");
    assertThat(info.enableExperimentalImport).isFalse();
    assertThat(info.id).isNull();
  }

  @Test
  void constructor5Args_setsIdAndInheritsOtherFields() {
    ChouetteInfo info = new ChouetteInfo(
      42L,
      "rb_nsr",
      "ref-abc",
      "Org",
      "admin"
    );
    assertThat(info.id).isEqualTo(42L);
    assertThat(info.xmlns).isEqualTo("rb_nsr");
    assertThat(info.referential).isEqualTo("ref-abc");
    assertThat(info.organisation).isEqualTo("Org");
    assertThat(info.user).isEqualTo("admin");
    assertThat(info.enableExperimentalImport).isFalse();
  }

  @Test
  void constructor6Args_setsAllFieldsIncludingExperimentalImport() {
    ChouetteInfo info = new ChouetteInfo(
      7L,
      "rb_x",
      "ref-x",
      "OrgX",
      "userX",
      true
    );
    assertThat(info.id).isEqualTo(7L);
    assertThat(info.xmlns).isEqualTo("rb_x");
    assertThat(info.referential).isEqualTo("ref-x");
    assertThat(info.organisation).isEqualTo("OrgX");
    assertThat(info.user).isEqualTo("userX");
    assertThat(info.enableExperimentalImport).isTrue();
  }

  // --- equals ---

  @Test
  void equals_sameInstance_returnsTrue() {
    ChouetteInfo info = new ChouetteInfo(1L, "rb", "ref", "org", "u");
    assertThat(info).isEqualTo(info);
  }

  @Test
  void equals_equivalentObject_returnsTrue() {
    ChouetteInfo a = new ChouetteInfo(1L, "rb", "ref", "org", "u");
    ChouetteInfo b = new ChouetteInfo(1L, "rb", "ref", "org", "u");
    assertThat(a).isEqualTo(b);
  }

  @Test
  void equals_null_returnsFalse() {
    ChouetteInfo info = new ChouetteInfo(1L, "rb", "ref", "org", "u");
    assertThat(info).isNotEqualTo(null);
  }

  @Test
  void equals_differentId_returnsFalse() {
    ChouetteInfo a = new ChouetteInfo(1L, "rb", "ref", "org", "u");
    ChouetteInfo b = new ChouetteInfo(2L, "rb", "ref", "org", "u");
    assertThat(a).isNotEqualTo(b);
  }

  @Test
  void equals_differentXmlns_returnsFalse() {
    ChouetteInfo a = new ChouetteInfo(1L, "rb_a", "ref", "org", "u");
    ChouetteInfo b = new ChouetteInfo(1L, "rb_b", "ref", "org", "u");
    assertThat(a).isNotEqualTo(b);
  }

  @Test
  void equals_differentReferential_returnsFalse() {
    ChouetteInfo a = new ChouetteInfo(1L, "rb", "ref-a", "org", "u");
    ChouetteInfo b = new ChouetteInfo(1L, "rb", "ref-b", "org", "u");
    assertThat(a).isNotEqualTo(b);
  }

  @Test
  void equals_differentOrganisation_returnsFalse() {
    ChouetteInfo a = new ChouetteInfo(1L, "rb", "ref", "org-a", "u");
    ChouetteInfo b = new ChouetteInfo(1L, "rb", "ref", "org-b", "u");
    assertThat(a).isNotEqualTo(b);
  }

  @Test
  void equals_differentUser_returnsFalse() {
    ChouetteInfo a = new ChouetteInfo(1L, "rb", "ref", "org", "u-a");
    ChouetteInfo b = new ChouetteInfo(1L, "rb", "ref", "org", "u-b");
    assertThat(a).isNotEqualTo(b);
  }

  @Test
  void equals_differentEnableExperimentalImport_returnsFalse() {
    ChouetteInfo a = new ChouetteInfo(1L, "rb", "ref", "org", "u", false);
    ChouetteInfo b = new ChouetteInfo(1L, "rb", "ref", "org", "u", true);
    assertThat(a).isNotEqualTo(b);
  }

  // --- hashCode ---

  @Test
  void hashCode_equalObjects_produceSameHash() {
    ChouetteInfo a = new ChouetteInfo(1L, "rb", "ref", "org", "u");
    ChouetteInfo b = new ChouetteInfo(1L, "rb", "ref", "org", "u");
    assertThat(a.hashCode()).isEqualTo(b.hashCode());
  }

  // --- toString ---

  @Test
  void toString_containsAllFields() {
    ChouetteInfo info = new ChouetteInfo(
      1L,
      "rb_nsr",
      "ref",
      "OrgName",
      "admin"
    );
    String s = info.toString();
    assertThat(s)
      .contains("id=1")
      .contains("xmlns='rb_nsr'")
      .contains("referential='ref'")
      .contains("organisationDTO='OrgName'")
      .contains("user='admin'");
  }

  @Test
  void toString_showsCorrectEnableAutoValidationValue() {
    ChouetteInfo info = new ChouetteInfo();
    info.enableAutoImport = false;
    info.enableAutoValidation = true;
    assertThat(info.toString()).contains("enableAutoValidation='true'");
  }
}
