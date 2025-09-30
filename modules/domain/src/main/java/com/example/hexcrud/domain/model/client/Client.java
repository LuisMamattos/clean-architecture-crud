package com.example.hexcrud.domain.model.client;

import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.hexcrud.domain.exception.DomainValidationException;

@Document(collection = "client")
public class Client {
    private String id;
    private String name;
    private String email;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private Client(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static Client create(String name, String email) {
        validate(name, email);
        return new Client(null, name, email);
    }

    public void updateDetails(String newName, String newEmail) {
        validate(newName, newEmail);
        this.name = newName;
        this.email = newEmail;
    }

    private static void validate(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainValidationException("Client name cannot be empty.");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainValidationException("Client email is not valid.");
        }
    }

    // Getters públicos...
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}