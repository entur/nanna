package no.entur.nanna.nanna.provider.usercontext.domain;

/**
 * Represent information about the current user.
 */
public record UserContext(
  String preferredName,
  boolean isRouteDataAdmin,
  boolean isOrganisationAdmin
) {}
