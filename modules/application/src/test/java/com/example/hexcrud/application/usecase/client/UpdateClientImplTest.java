package com.example.hexcrud.application.usecase.client;

import java.lang.reflect.Field;
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
import com.example.hexcrud.domain.exception.ResourceNotFoundException;
import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;

@ExtendWith(MockitoExtension.class)
class UpdateClientImplTest {
    
    @Mock private ClientRepository clientRepository;
    @InjectMocks private UpdateClientImpl updateClient;

    // --- Helper para injetar IDs nos testes ---
    private void setId(Object target, String id) {
        try {
            Field idField = target.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(target, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set ID for test", e);
        }
    }

    @Test
    @DisplayName("Should update client successfully")
    void shouldUpdateClientSuccessfully() {
        // Arrange
        String clientId = "client-123";
        var input = new UpdateClient.Input(clientId, "New Name", "new@example.com");
        
        Client clientFromDb = Client.create("Old Name", "old@example.com");
        setId(clientFromDb, clientId); // Atribuímos o ID para simular um objeto persistido
        
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientFromDb));
        when(clientRepository.findByEmail(input.email())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Client result = updateClient.execute(input);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getEmail()).isEqualTo("new@example.com");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for non-existent client")
    void shouldThrowNotFoundForNonExistentClient() {
        // Arrange
        String nonExistentId = "non-existent-id";
        var input = new UpdateClient.Input(nonExistentId, "New Name", "new@example.com");
        when(clientRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> updateClient.execute(input))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when email belongs to another client")
    void shouldThrowConflictWhenEmailBelongsToAnother() {
        // Arrange
        String clientToUpdateId = "client-123";
        String otherClientId = "client-456";
        var input = new UpdateClient.Input(clientToUpdateId, "New Name", "existing@example.com");
        
        Client clientToUpdate = Client.create("Old Name", "old@example.com");
        setId(clientToUpdate, clientToUpdateId); // Objeto que estamos atualizando

        Client otherClientWithSameEmail = Client.create("Other Client", "existing@example.com");
        setId(otherClientWithSameEmail, otherClientId); // Objeto que já possui o e-mail
        
        when(clientRepository.findById(clientToUpdateId)).thenReturn(Optional.of(clientToUpdate));
        when(clientRepository.findByEmail(input.email())).thenReturn(Optional.of(otherClientWithSameEmail));

        // Act & Assert
        assertThatThrownBy(() -> updateClient.execute(input))
                .isInstanceOf(BusinessRuleException.class);

        verify(clientRepository, never()).save(any(Client.class));
    }
}