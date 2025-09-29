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

import com.example.hexcrud.application.exception.BusinessRuleException;
import com.example.hexcrud.application.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;

@ExtendWith(MockitoExtension.class)
class UpdateClientImplTest {
    
    @Mock
    private ClientRepository clientRepository;
    
    @InjectMocks
    private UpdateClientImpl updateClient;

    @Test
    @DisplayName("Given an existing client and valid data, when updating, then should return the updated client")
    void givenExistingClientAndValidData_whenUpdating_thenShouldReturnUpdatedClient() {
        // Arrange
        String clientId = "client-id-123";
        var input = new UpdateClient.Input(clientId, "New Name", "new.email@example.com");
        Client clientFromDb = new Client(clientId, "Old Name", "old.email@example.com");
        
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientFromDb));
        when(clientRepository.existsByEmailAndIdNot(input.email(), clientId)).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Client result = updateClient.execute(input);

        // Assert: Verifica o estado do objeto retornado
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getEmail()).isEqualTo("new.email@example.com");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Given a non-existent client ID, when updating, then should throw ResourceNotFoundException")
    void givenNonExistentClientId_whenUpdating_thenShouldThrowResourceNotFoundException() {
        // Arrange
        String nonExistentId = "non-existent-id";
        var input = new UpdateClient.Input(nonExistentId, "New Name", "new.email@example.com");

        when(clientRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert: Verifica se a exceção correta é lançada
        assertThatThrownBy(() -> updateClient.execute(input))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Client not found");

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Given an email that exists for another client, when updating, then should throw BusinessRuleException")
    void givenEmailExistsForAnotherClient_whenUpdating_thenShouldThrowBusinessRuleException() {
        // Arrange
        String clientId = "client-id-123";
        var input = new UpdateClient.Input(clientId, "New Name", "existing.email@example.com");
        Client clientFromDb = new Client(clientId, "Old Name", "old.email@example.com");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientFromDb));
        // Simula que o email "existing.email@example.com" já pertence a outro cliente
        when(clientRepository.existsByEmailAndIdNot(input.email(), clientId)).thenReturn(true);

        // Act & Assert: Verifica se a exceção de regra de negócio é lançada
        assertThatThrownBy(() -> updateClient.execute(input))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Email already in use");

        verify(clientRepository, never()).save(any(Client.class));
    }
}