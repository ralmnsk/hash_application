package tt.authorization.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tt.authorization.dto.UserDTO;
import tt.authorization.service.UserService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    private UserDTO userDTO;
    private Long id;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setEmail("test@email.com");
        userDTO.setPassword("123");
        id = 1L;
    }

    @Test
    void saveTest() {
        when(userService.save(userDTO)).thenReturn(userDTO);
        UserDTO saved = userController.save(userDTO);
        verify(userService, times(1)).save(userDTO);
    }


    @Test
    void delete() {
        doNothing().when(userService).delete(id);
        ResponseEntity<Void> delete = userController.delete(id);
        Assertions.assertEquals(HttpStatus.OK, delete.getStatusCode());
    }
}