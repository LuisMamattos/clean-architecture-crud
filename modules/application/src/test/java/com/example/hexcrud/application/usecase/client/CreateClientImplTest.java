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

// @ExtendWith(MockitoExtension.class): Ativa a integração do JUnit 5 com o Mockito.
@ExtendWith(MockitoExtension.class)
class CreateClientImplTest {

    // @Mock: Diz ao Mockito para criar um "dublê" (mock) desta interface.
    // Este objeto não tem conexão com o banco; ele só fará o que mandarmos.
    @Mock
    private ClientRepository clientRepository;

    // @InjectMocks: Cria uma instância REAL da classe que queremos testar (CreateClientImpl)
    // e automaticamente injeta qualquer campo anotado com @Mock (neste caso, o clientRepository)
    // no seu construtor.
    @InjectMocks
    private CreateClientImpl createClient;

    @Test
    @DisplayName("Given a new client with a unique email, when creating, then it should be saved successfully")
    void given_aNewClientWithUniqueEmail_when_creating_then_itShouldBeSavedSuccessfully() {
        // Given (Arrange): Preparamos o cenário e o comportamento dos mocks.
        var input = new CreateClient.Input("Test User", "unique.email@example.com");
        var clientToBeSaved = new Client(input.name(), input.email());

        // Configuração do Mock:
        // "Quando o método findByEmail for chamado com este email específico,
        //  então retorne um Optional vazio (fingindo que o email é único)."
        when(clientRepository.findByEmail("unique.email@example.com")).thenReturn(Optional.empty());

        // "Quando o método save for chamado com qualquer objeto do tipo Client,
        //  então retorne o próprio cliente que foi passado como argumento."
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client clientPassedToSave = invocation.getArgument(0);
            clientPassedToSave.setId("mock-id-123"); // Simula o DB gerando um ID
            return clientPassedToSave;
        });

        // When (Act): Executamos o método que estamos testando.
        CreateClient.Output result = createClient.execute(input);

        // Then (Assert): Verificamos se o resultado e as interações foram as esperadas.
        assertThat(result).isInstanceOf(CreateClient.Output.Created.class);
        
        Client resultClient = ((CreateClient.Output.Created) result).client();
        assertThat(resultClient.getId()).isEqualTo("mock-id-123");
        assertThat(resultClient.getName()).isEqualTo("Test User");

        // Verificamos se o método 'save' do nosso mock foi chamado exatamente 1 vez.
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Given a client with an existing email, when creating, then should return an EmailAlreadyExists error")
    void given_aClientWithExistingEmail_when_creating_then_shouldReturnEmailAlreadyExistsError() {
        // Given
        var input = new CreateClient.Input("Another User", "existing.email@example.com");
        
        // Configuração do Mock:
        // "Quando o método findByEmail for chamado com este email,
        //  então retorne um Optional contendo um cliente qualquer (fingindo que o email já existe)."
        when(clientRepository.findByEmail("existing.email@example.com")).thenReturn(Optional.of(new Client()));
        
        // When
        CreateClient.Output result = createClient.execute(input);
        
        // Then
        assertThat(result).isInstanceOf(CreateClient.Output.EmailAlreadyExists.class);
        
        // Verificamos que, neste caso, o método save NUNCA foi chamado.
        verify(clientRepository, never()).save(any(Client.class));
    }
}