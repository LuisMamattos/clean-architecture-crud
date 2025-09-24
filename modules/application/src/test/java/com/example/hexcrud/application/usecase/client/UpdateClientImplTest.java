package com.example.hexcrud.application.usecase.client;

import com.example.hexcrud.domain.model.client.Client;
import com.example.hexcrud.domain.repository.client.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateClientImplTest {
    
    @Mock
    private ClientRepository clientRepository;
   
    @InjectMocks
    private UpdateClientImpl updateClient;

    @Test
    @DisplayName("Given an existing client, when updating, then should update and return the client")
    void given_anExistingClient_when_updating_then_shouldUpdateAndReturnClient() {
        String clientId = "client-id-123";
        var input = new UpdateClient.Input(clientId, "New Name", "new.email@example.com");

        Client clientFromDb = new Client("Old Name", "old.email@example.com");
        clientFromDb.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientFromDb));

        when(clientRepository.findByEmail("new.email@example.com")).thenReturn(Optional.empty());
        
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));


        UpdateClient.Output result = updateClient.execute(input);


        assertThat(result).isInstanceOf(UpdateClient.Output.Updated.class);
        
        Client updatedClient = ((UpdateClient.Output.Updated) result).client();
        assertThat(updatedClient.getName()).isEqualTo("New Name");
        assertThat(updatedClient.getEmail()).isEqualTo("new.email@example.com");

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Given a non-existent client ID, when updating, then should return a 'Not Found' output")
    void given_aNonExistentClientId_when_updating_then_shouldReturnNotFound() {
        // Given
        String nonExistentId = "non-existent-id";
        var input = new UpdateClient.Input(nonExistentId, "New Name", "new.email@example.com");

        when(clientRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        UpdateClient.Output result = updateClient.execute(input);

        // Then
        assertThat(result).isInstanceOf(UpdateClient.Output.NotFound.class);

        verify(clientRepository, never()).save(any(Client.class));
    }
}