package tt.hashtranslator.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tt.hashtranslator.dto.HashRequestDTO;
import tt.hashtranslator.dto.HashResponseDTO;
import tt.hashtranslator.service.HashService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HashControllerTest {
    @InjectMocks
    private HashController hashController;
    @Mock
    private HashService hashService;

    private HashRequestDTO hashRequestDTO;
    private HashResponseDTO hashResponseDTO;
    private String id;

    @BeforeEach
    void setUp() {
        hashRequestDTO = new HashRequestDTO();
        Set<String> hashes = new HashSet<>();
        hashes.add("c81e728d9d4c2f636f067f89cc14862c");
        hashes.add("eccbc87e4b5ce2fe28308fd9f2a7baf3");
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
    }

    @Test
    void sendRequest() {
        ResponseEntity<String> stringResponseEntity = hashController.sendRequest(hashRequestDTO);
        Assertions.assertEquals(HttpStatus.ACCEPTED, stringResponseEntity.getStatusCode());
    }

    @Test
    void getRequestResult() {
        when(hashService.getRequestResult(id)).thenReturn(hashResponseDTO);
        HashResponseDTO requestResult = hashController.getRequestResult(id);
        Assertions.assertEquals(id, requestResult.getUuid());
        Assertions.assertTrue(requestResult.getHashes().get(0).containsKey("c4ca4238a0b923820dcc509a6f75849b"));
        Assertions.assertTrue(requestResult.getHashes().get(0).containsValue("1"));
    }
}