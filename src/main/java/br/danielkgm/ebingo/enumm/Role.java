package br.danielkgm.ebingo.enumm;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private String value;

    Role(String role) {
        this.value = role;
    }

    public String getRole() {
        return this.value;
    }
}
