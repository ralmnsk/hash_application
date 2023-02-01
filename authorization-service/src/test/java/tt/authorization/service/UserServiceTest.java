package tt.authorization.service;

import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import tt.authorization.dto.UserDTO;
import tt.authorization.model.User;
import tt.authorization.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private Mapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserDTO userDTO;
    private User user;

    private Long id;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setEmail("test@email.com");
        userDTO.setPassword("123");

        user = new User();
        user.setEmail("test@email.com");
        user.setPassword("123");
        user.setIsActive(true);

        id = 1L;
    }

    @Test
    void save() {
        when(mapper.map(userDTO, User.class)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("111");
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.map(user, UserDTO.class)).thenReturn(userDTO);

        userService.save(userDTO);

        verify(mapper, times(1)).map(userDTO, User.class);
        verify(mapper, times(1)).map(user, UserDTO.class);
        verify(passwordEncoder, times(1)).encode("123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void delete() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        userService.delete(id);
        Assertions.assertFalse(user.getIsActive());
    }
}