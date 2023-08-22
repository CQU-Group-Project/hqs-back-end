package com.cqu.hqs.service;

import com.cqu.hqs.Repository.UserRepository;
import com.cqu.hqs.dto.GuestDto;
import com.cqu.hqs.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deependra Karki
 */
@Service
public class UserService {

    private UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    User createUser(GuestDto guestDto) {
        User user = new User();
        user.setUsername(guestDto.getEmail());
        user.setPassword(guestDto.getPassword());
        user.setRole(String.valueOf(guestDto.getRole()));
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

}
