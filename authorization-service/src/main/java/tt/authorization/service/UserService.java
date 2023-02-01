package tt.authorization.service;

import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tt.authorization.dto.UserDTO;
import tt.authorization.exception.NotFoundException;
import tt.authorization.model.Role;
import tt.authorization.model.User;
import tt.authorization.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class UserService {
    @Value("${application.admin.name}")
    private String admin;
    @Value("${application.admin.password}")
    private String pass;
    private static final String PROTECTED = "[protected]";
    private final Mapper mapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(Mapper mapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UserDTO save(UserDTO userDTO) {
        User user = mapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setIsActive(true);
        User saved = userRepository.save(user);
        userDTO = mapper.map(saved, UserDTO.class);
        userDTO.setPassword(PROTECTED);
        return userDTO;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.findById(id).ifPresent(u -> u.setIsActive(false));
    }

    public UserDTO findByEmail(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found:" + name));
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new NotFoundException("User not found:" + name);
        }
        user.setPassword(PROTECTED);
        return mapper.map(user, UserDTO.class);
    }

    @PostConstruct
    private void postConstruct() {
        Optional<User> byEmail = userRepository.findByEmail(admin);
        if (byEmail.isEmpty()) {
            User user = new User();
            user.setRole(Role.ADMIN);
            user.setPassword(passwordEncoder.encode(pass));
            user.setEmail(admin);
            user.setIsActive(true);
            userRepository.save(user);
        }
    }

}
