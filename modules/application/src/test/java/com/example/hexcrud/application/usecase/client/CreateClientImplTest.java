package com.example.hexcrud.application.usecase.client;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.hexcrud.domain.exception.BusinessRuleException;
import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;

@ExtendWith(MockitoExtension.class)
class CreateClientImplTest {

    @Mock private ClientRepository clientRepository;
    @InjectMocks private CreateClientImpl createClient;

    @Test
    @DisplayName("Should create client when email is unique")
    void shouldCreateClientWhenEmailIsUnique() {
        // Arrange
        var input = new CreateClient.Input("Test User", "unique@example.com");
        when(clientRepository.findByEmail(input.email())).thenReturn(Optional.empty());
        // Mock the save to return the object with an ID
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            // In a real scenario, reflection or a package-private setter might be used.
            // For a test, this is a simple way to simulate ID assignment.
            // A better way would be Test Data Builders. For now, let's assume we can't set the ID.
            return client;
        });

        // Act
        Client result = createClient.execute(input);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test User");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Arrange
        var input = new CreateClient.Input("Another User", "existing@example.com");
        // Mocking the return of an existing client object
        when(clientRepository.findByEmail(input.email())).thenReturn(Optional.of(Client.create("Some Client", "existing@example.com")));
        
        // Act & Assert
        assertThatThrownBy(() -> createClient.execute(input))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Email already in use");
        
        verify(clientRepository, never()).save(any(Client.class));
    }
}