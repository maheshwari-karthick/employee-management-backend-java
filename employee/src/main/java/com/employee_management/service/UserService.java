package com.employee_management.service;

import com.employee_management.model.User;
import com.employee_management.repository.UserRepository;
import com.employee_management.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<GetUserResponse> findAllUser() {
        List<User> users = userRepository.findAll();
        List<GetUserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(new GetUserResponse(user));
        }
        return userResponses;
    }

    public long addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).getId();
    }

    public GetUserResponse findUserById(long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new GetUserResponse(user.get());
        }
        throw new NotFoundException();
    }

    public long updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).getId();
    }

    @Override
    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isPresent()) {
            return User.builder()
                    .username(user.get().getUsername())
                    .password(user.get().getPassword())
                    .roles(user.get().getRoles())
                    .build();
        }
        throw new UsernameNotFoundException("User not found for username: " + userName);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public boolean existsUser(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}
