package com.employee_management.service;

import com.employee_management.model.Role;
import com.employee_management.model.User;
import com.employee_management.repository.UserRepository;
import com.employee_management.response.GetUserResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {
    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void shouldFindAllUser() {
        User user1 = new User(1l,"Mahi","Mahi",List.of(Role.ADMIN));
        User user2 = new User(2l,"Test","Test",List.of(Role.ADMIN));

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<GetUserResponse> actualAllUsers = userService.findAllUser();

        verify(userRepository).findAll();
        assertFalse(actualAllUsers.isEmpty());
    }

    @Test
    void shouldAddUser() {
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        User user = new User(1l,"Mahi","Mahi",List.of(Role.ADMIN));
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        long actualAddUserResult = userService.addUser(user);

        verify(userRepository).save(isA(User.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
        assertEquals("secret", user.getPassword());
        assertEquals(1L, actualAddUserResult);
    }

    @Test
    public void shouldFindUserById() throws ChangeSetPersister.NotFoundException {

        User user = new User(1l,"Mahi","Mahi",List.of(Role.ADMIN));
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));

        GetUserResponse actualUser = userService.findUserById(1l);

        verify(userRepository).findById(1l);
        assertFalse(actualUser.getRoles().isEmpty());
    }
    @Test
    public void shouldFindUserByIdThrowsException() throws Exception {

        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(ChangeSetPersister.NotFoundException.class, () -> userService.findUserById(1L));
        verify(userRepository).findById(eq(1L));
    }

    @Test
    public void shouldUpdateUser() {

        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        User user = new User(1l,"Mahi","Mahi",List.of(Role.ADMIN));
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        long actualUpdateUserResult = userService.updateUser(user);

        verify(userRepository).save(isA(User.class));
        assertEquals("Mahi", user.getUsername());
        assertEquals(1L, actualUpdateUserResult);
    }


    @Test
    public void shouldLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User(1l,"Mahi","Mahi",List.of(Role.ADMIN));
        when(userRepository.findByUsername("Mahi")).thenReturn(Optional.of(user));

        User actualUser = userService.loadUserByUsername("Mahi");

        verify(userRepository).findByUsername("Mahi");
        assertFalse(actualUser.getRoles().isEmpty());
    }

    @Test
    public void shouldLoadUserByUsernameThrowsException() throws UsernameNotFoundException {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("Mahi"));
        verify(userRepository).findByUsername(eq("Mahi"));
    }


    @Test
    public void shouldDeleteUser()  {
        doNothing().when(userRepository).deleteById(Mockito.<Long>any());
        userService.deleteUser(1L);
        verify(userRepository).deleteById(eq(1L));
        assertTrue(userService.findAllUser().isEmpty());
    }
    @Test
   public void shouldReturnExistsUser() {
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        boolean actualExistsUserResult = userService.existsUser("Mahi");
        verify(userRepository).existsByUsername(eq("Mahi"));
        assertTrue(actualExistsUserResult);
    }

}
