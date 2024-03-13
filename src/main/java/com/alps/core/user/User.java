package com.alps.core.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a user with an ID, name, and email address.
 * Ensures that the email provided is in a valid format.
 */
@Getter
@EqualsAndHashCode
public class User {
    private final String id;
    private final String name;
    private final String email;

    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    /**
     * Constructs a new User instance.
     * 
     * @param id    The unique identifier for the user.
     * @param name  The user's name.
     * @param email The user's email address. Must be in a valid format.
     * @throws IllegalArgumentException if the email address is not in a valid
     *                                  format.
     */
    private User(
            @NonNull String id,
            @NonNull String name,
            @NonNull String email) {

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }

        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Creates and returns a new User instance.
     * 
     * @param id    The unique identifier for the user.
     * @param name  The user's name.
     * @param email The user's email address. Must be in a valid format.
     * @return A new User instance with the provided id, name, and email.
     * @throws IllegalArgumentException if the email address is not in a valid
     *                                  format.
     */
    public static User create(
            @NonNull String id,
            @NonNull String name,
            @NonNull String email) {

        return new User(
                id,
                name,
                email);
    }

    /**
     * Returns a new User instance with the specified name, retaining the original
     * id and email.
     * 
     * @param newName The new name for the user.
     * @return A new User instance with the updated name.
     */
    public User withName(String newName) {
        return new User(this.id, newName, this.email);
    }

    /**
     * Returns a new User instance with the specified email, retaining the original
     * id and name.
     * 
     * @param newEmail The new email for the user. Must be in a valid format.
     * @return A new User instance with the updated email.
     * @throws IllegalArgumentException if the new email address is not in a valid
     *                                  format.
     */
    public User withEmail(String newEmail) {
        if (!isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email");
        }
        return new User(this.id, this.name, newEmail);
    }

    /**
     * Validates the format of the given email address.
     * 
     * @param email The email address to validate.
     * @return true if the email address is in a valid format; false otherwise.
     */
    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}
