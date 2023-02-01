package tt.hashtranslator.service;

import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import tt.hashtranslator.dto.HashRequestDTO;
import tt.hashtranslator.dto.HashResponseDTO;
import tt.hashtranslator.model.Hash;
import tt.hashtranslator.model.HashRequest;
import tt.hashtranslator.repository.HashRequestRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HashServiceTest {
    @InjectMocks
    private HashService hashService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Mapper mapper;
    @Mock
    private HashRequestRepository hashRequestRepository;
    @Mock
    private HashDecryptService hashDecryptService;
    private HashRequestDTO hashRequestDTO;
    private HashResponseDTO hashResponseDTO;
    private String id;
    private Hash hashEntity;

    @BeforeEach
    void setUp() {
        hashRequestDTO = new HashRequestDTO();
        Set<String> hashes = new HashSet<>();
        hashes.add("c4ca4238a0b923820dcc509a6f75849b");
        hashRequestDTO.setHashes(hashes);

        id = "test-id";
        hashResponseDTO = new HashResponseDTO();
        hashResponseDTO.setUuid(id);

        List<Map<String, String>> hashesList = new ArrayList<>();
        Map<String, String> hash = new HashMap<>();
        hash.put("c4ca4238a0b923820dcc509a6f75849b", "1");
        hashesList.add(hash);
        hashResponseDTO.setHashes(hashesList);

        hashEntity = new Hash();
        hashEntity.setMd5("c4ca4238a0b923820dcc509a6f75849b");
        hashEntity.setDecoded(true);
        hashEntity.setProcessed(true);
        hashEntity.setResult("1");
    }

    @Test
    void requestHash() {
        when(hashDecryptService.getDecryptedHash("c4ca4238a0b923820dcc509a6f75849b")).thenReturn(hashEntity);

        HashRequest hashRequest = new HashRequest();
        when(mapper.map(hashRequestDTO, HashRequest.class)).thenReturn(hashRequest);
        when(hashRequestRepository.insert(hashRequest)).thenReturn(hashRequest);

        hashService.requestHash(hashRequestDTO);
        await().until(() -> {
            hashRequestRepository.insert(hashRequest);
            return true;
        });
        verify(hashDecryptService).getDecryptedHash("c4ca4238a0b923820dcc509a6f75849b");
        verify(hashRequestRepository, times(2)).insert(hashRequest);
    }

    @Test
    void requestHashDecryptServiceThrowsException() {

        when(hashDecryptService.getDecryptedHash("c4ca4238a0b923820dcc509a6f75849b")).thenThrow(new RuntimeException("test"));

        HashRequest hashRequest = new HashRequest();
            hashService.requestHash(hashRequestDTO);
            await().until(() -> {
                hashRequestRepository.insert(hashRequest);
                return true;
            });

        verify(hashRequestRepository).insert(hashRequest);
    }

    @Test
    void getRequestResult() {
        HashRequest hashRequest = new HashRequest();
        hashRequest.setHashes(new ArrayList<>());
        Optional<HashRequest> request = Optional.of(hashRequest);
        when(hashRequestRepository.findById(id)).thenReturn(request);
        when(mapper.map(hashRequest, HashResponseDTO.class)).thenReturn(hashResponseDTO);
        hashService.getRequestResult(id);

        verify(hashRequestRepository, times(1)).findById(id);
        verify(mapper, times(1)).map(hashRequest, HashResponseDTO.class);
    }
}