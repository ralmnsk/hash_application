package tt.hashtranslator.service;

import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tt.hashtranslator.dto.HashRequestDTO;
import tt.hashtranslator.dto.HashResponseDTO;
import tt.hashtranslator.exception.AsyncException;
import tt.hashtranslator.exception.NotFoundException;
import tt.hashtranslator.model.Hash;
import tt.hashtranslator.model.HashRequest;
import tt.hashtranslator.repository.HashRequestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashService {
    private final Mapper mapper;
    private final HashRequestRepository hashRequestRepository;
    private final HashDecryptService hashDecryptService;

    @Async
    public void requestHash(HashRequestDTO hashRequestDTO) {
        CompletableFuture.supplyAsync(() ->
                        hashRequestDTO.getHashes()
                                .stream()
                                .map(hashDecryptService::getDecryptedHash)
                                .collect(Collectors.toList()))
                .thenAccept(hashes -> saveHashes(hashRequestDTO, hashes))
                .exceptionally(throwable -> {
                    log.warn("Async hash request execution failed:{}", throwable.getMessage());
                    return null;
                });
    }

    public HashResponseDTO getRequestResult(String id) {
        Optional<HashRequest> hashResponse = hashRequestRepository.findById(id);
        HashRequest hashRequest = hashResponse.orElseThrow(() -> new NotFoundException("Not found id:" + id));
        HashResponseDTO response = mapper.map(hashRequest, HashResponseDTO.class);
        List<Map<String, String>> hashes = hashRequest.getHashes().stream().map(h -> {
            Map<String, String> map = new HashMap<>();
            map.put(h.getMd5(), h.getResult());
            return map;
        }).collect(Collectors.toList());
        response.setHashes(hashes);
        return response;
    }

    private void saveHashes(HashRequestDTO hashRequestDTO, List<Hash> hashes) {
        HashRequest request = mapper.map(hashRequestDTO, HashRequest.class);
        request.setHashes(hashes);
        HashRequest insert = hashRequestRepository.insert(request);
        log.info("hash request saved:{}", insert);
    }

}
