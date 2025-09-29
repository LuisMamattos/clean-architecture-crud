package com.example.hexcrud.application.usecase.client;

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

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private CreateClientImpl createClient;

    @Test
    @DisplayName("Given valid data for a new client, when creating, then should return the created client")
    void givenValidData_whenCreatingClient_thenShouldReturnCreatedClient() {
        // Arrange
        var input = new CreateClient.Input("Test User", "unique.email@example.com");
        Client savedClient = new Client("mock-id-123", input.name(), input.email());

        // Configuração dos Mocks
        when(clientRepository.existsByEmail(input.email())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        // Act
        Client result = createClient.execute(input);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("mock-id-123");
        assertThat(result.getName()).isEqualTo("Test User");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Given an existing email, when creating a client, then should throw BusinessRuleException")
    void givenExistingEmail_whenCreatingClient_thenShouldThrowBusinessRuleException() {
        // Arrange
        var input = new CreateClient.Input("Another User", "existing.email@example.com");
        
        when(clientRepository.existsByEmail(input.email())).thenReturn(true);
        
        // Act & Assert
        assertThatThrownBy(() -> createClient.execute(input))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Email already in use");
        
        verify(clientRepository, never()).save(any(Client.class));
    }
}