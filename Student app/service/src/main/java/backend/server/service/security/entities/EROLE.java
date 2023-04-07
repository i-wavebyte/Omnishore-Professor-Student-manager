package backend.server.service.security.entities;

/**
 * This is an enum that defines the possible roles for users in the system
 */
public enum EROLE {
    ROLE_PROF_MANAGER, // The user has basic access to the system
    ROLE_STUD_MANAGER, // The user has elevated privileges to moderate content
}