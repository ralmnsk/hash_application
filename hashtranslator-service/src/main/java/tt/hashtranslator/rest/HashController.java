package tt.hashtranslator.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tt.hashtranslator.dto.HashRequestDTO;
import tt.hashtranslator.dto.HashResponseDTO;
import tt.hashtranslator.service.HashService;
import tt.hashtranslator.utils.HashValidationUtil;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/applications")
@AllArgsConstructor
public class HashController {
    private HashService hashService;

    @PostMapping
    public ResponseEntity<String> sendRequest(@Valid @RequestBody HashRequestDTO requestDTO) {
        String uuid = UUID.randomUUID().toString();
        requestDTO.setUuid(uuid);
        HashValidationUtil.validate(requestDTO);
        hashService.requestHash(requestDTO);
        log.info("request id:{} created", uuid);
        return new ResponseEntity<>(uuid, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public HashResponseDTO getRequestResult(@PathVariable("id") String id) {
        return hashService.getRequestResult(id);
    }
}
