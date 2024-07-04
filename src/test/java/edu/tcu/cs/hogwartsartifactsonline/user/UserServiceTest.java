package edu.tcu.cs.hogwartsartifactsonline.user;


import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// For JUnit 5, we need to use @ExtendWith
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    List<UserEntity> users;

    @BeforeEach
    void setUp() {
        UserEntity user1 = new UserEntity();
        user1.setId(1);
        user1.setUsername("john");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("admin user");

        UserEntity user2 = new UserEntity();
        user2.setId(2);
        user2.setUsername("eric");
        user2.setPassword("654321");
        user2.setEnabled(true);
        user2.setRoles("user");

        UserEntity user3 = new UserEntity();
        user3.setId(3);
        user3.setUsername("tom");
        user3.setPassword("qwerty");
        user3.setEnabled(false);
        user3.setRoles("user");

        this.users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindAllSuccess(){
        // Given. Arrange inputs and targets. Define the behavior of Mock object userRepository.
        given(this.userRepository.findAll()).willReturn(this.users);

        // When. Act on the target behavior. When steps should cover the method to be tested.
        List<UserEntity> actualUsers = this.userService.findAll();

        // Then. Assert expected outcomes
        assertThat(actualUsers.size()).isEqualTo(this.users.size());

        // Verify userRepository.findAll() is called exactly
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindUserByIdSuccess(){
        UserEntity user1 = new UserEntity();
        user1.setId(1);
        user1.setUsername("john");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("admin user");

        // Given. Arrange inputs and targets. Define the behaviour of Mock object userRepository
        given(this.userRepository.findById(1)).willReturn(Optional.of(user1));

        // When. Act on the target behavior.
        UserEntity returnedUser = this.userService.findUserById(1);

        // Then. Assert expected outcomes
        assertThat(returnedUser.getId()).isEqualTo(user1.getId());
        assertThat(returnedUser.getUsername()).isEqualTo(user1.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(user1.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(user1.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(user1.getRoles());

        // Verify
        verify(userRepository, times(1)).findById(user1.getId());

    }

    @Test
    void testFindUserByIdNotFound(){
        // Given. Arrange inputs and targets. Define the behaviour of Mock object userRepository
        given(this.userRepository.findById(1)).willReturn(Optional.empty());

        // When. Act on the target behavior.
        Throwable thrown = catchThrowable(() -> {
           UserEntity returnedUser = this.userService.findUserById(1);
        });

        // Then. Assert expected outcomes
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id: 1");

        // Verify
        verify(userRepository, times(1)).findById(1);

    }

    @Test
    void testSaveSuccess(){
        UserEntity newUser = new UserEntity();
        newUser.setUsername("vinh");
        newUser.setPassword("vinh291");
        newUser.setEnabled(true);
        newUser.setRoles("user");

        // Given
        given(this.passwordEncoder.encode(newUser.getPassword())).willReturn("Encoded Password");
        given(this.userRepository.save(newUser)).willReturn(newUser);

        // When
        UserEntity savedUser = this.userService.save(newUser);

        // Then
        assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(savedUser.isEnabled()).isEqualTo(newUser.isEnabled());
        assertThat(savedUser.getRoles()).isEqualTo(newUser.getRoles());

        // Verify
        verify(userRepository, times(1)).save(newUser);

    }

    @Test
    void testUpdateUserSuccess(){
        UserEntity oldUser = new UserEntity();
        oldUser.setId(1);
        oldUser.setUsername("john");
        oldUser.setPassword("123456");
        oldUser.setEnabled(true);
        oldUser.setRoles("admin user");

        UserEntity updateContent = new UserEntity();
        updateContent.setUsername("john - after updated");
        updateContent.setPassword("123456789");
        updateContent.setEnabled(true);
        updateContent.setRoles("user");

        // Given
        given(this.userRepository.findById(1)).willReturn(Optional.of(oldUser));
        given(this.userRepository.save(oldUser)).willReturn(oldUser);

        // When
        UserEntity updatedUser = this.userService.update(1,updateContent);

        // Then
        assertThat(updatedUser.getId()).isEqualTo(oldUser.getId());
        assertThat(updatedUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(updatedUser.getPassword()).isEqualTo(oldUser.getPassword());
        assertThat(updatedUser.isEnabled()).isEqualTo(oldUser.isEnabled());
        assertThat(updatedUser.getRoles()).isEqualTo(oldUser.getRoles());

        // Verify
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(oldUser);

    }

    @Test
    void testUpdateUserNotFound(){
        // Given
        given(this.userRepository.findById(7)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.update(7,new UserEntity());
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id: 7");

        // Verify
        verify(userRepository, times(1)).findById(7);
    }

    @Test
    void testDeleteSuccess(){
        // Given
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("john");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1);

        // When
        this.userService.delete(1);

        // Then
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound(){
        // Given
        given(this.userRepository.findById(5)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.delete(5);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id: 5");

        // Verify
        verify(userRepository, times(1)).findById(5);
    }
}
