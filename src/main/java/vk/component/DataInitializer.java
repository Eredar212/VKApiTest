package vk.component;

import vk.dto.UserCreateDTO;
import vk.mapper.UserMapper;
import vk.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userData = new UserCreateDTO();
        userData.setName("admin");
        userData.setEmail("admin@example.com");
        userData.setPasswordDigest("admin");
        var user = userMapper.map(userData);
        userRepository.save(user);
    }
}
