package com.alps.core.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class User {
    private final String id;
    private final String name;
    private final String email;

    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

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

    public static User create(
            @NonNull String id,
            @NonNull String name,
            @NonNull String email) {

        return new User(
                id,
                name,
                email);
    }

    public User withName(String newName) {
        return new User(this.id, newName, this.email);
    }

    public User withEmail(String newEmail) {
        if (!isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email");
        }
        return new User(this.id, this.name, newEmail);
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}
