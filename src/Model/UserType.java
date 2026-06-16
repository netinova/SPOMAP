
package Model;

/**
 * Enum representing the type of user in the system.
 * Used to distinguish between different user roles and permissions.
 */
public enum UserType {
    NORMAL("Normal User", "USR"),
    PRIME("Prime Member", "USR"),
    ADMIN("Administrator", "ADM");

    private final String displayName;
    private final String idPrefix;

    UserType(String displayName, String idPrefix) {
        this.displayName = displayName;
        this.idPrefix = idPrefix;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    /**
     * Check if this user type has administrative privileges
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Check if this user type has prime membership benefits
     */
    public boolean isPrime() {
        return this == PRIME;
    }

}
