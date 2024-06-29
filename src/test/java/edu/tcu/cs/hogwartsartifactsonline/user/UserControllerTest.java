package edu.tcu.cs.hogwartsartifactsonline.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.user.dto.UserDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    List<UserEntity> users;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

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
        this.users.add(user1);
        this.users.add(user2);
        this.users.add(user3);
    }

    @AfterEach
    void tearDown() {
        
    }

    @Test
    void testFindAllUsersSuccess() throws Exception{

        // Given
        given(this.userService.findAll()).willReturn(this.users);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.users.size())))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("john"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].username").value("eric"));

    }

    @Test
    void testFindUserByIdSuccess() throws Exception{
        // Given
        given(this.userService.findUserById(1)).willReturn(this.users.get(0));

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find User By Id Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("john"));
    }

    @Test
    void testFindUserByIdNotFound() throws Exception{
        // Given
        given(this.userService.findUserById(5)).willThrow(new ObjectNotFoundException("user", 5));

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id: 5"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddUserSuccess() throws Exception{

        UserEntity newUser = new UserEntity();
        newUser.setId(4);
        newUser.setUsername("vinh");
        newUser.setPassword("vinh291");
        newUser.setEnabled(true);
        newUser.setRoles("admin user"); // The delimiter is space

        String json = this.objectMapper.writeValueAsString(newUser);

        // Given
        given(this.userService.save(Mockito.any(UserEntity.class))).willReturn(newUser);

        // When and Then
        this.mockMvc.perform(post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add User Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("vinh"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("admin user"));
    }

    @Test
    void testUpdateSuccess() throws Exception{

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(3);
        updatedUser.setUsername("tom123");
        updatedUser.setEnabled(false);
        updatedUser.setRoles("user");

        String json = this.objectMapper.writeValueAsString(updatedUser);

        // Given
        given(this.userService.update(eq(3), Mockito.any(UserEntity.class))).willReturn(updatedUser);

        // When and Then
        this.mockMvc.perform(put(this.baseUrl + "/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update User Success"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.username").value("tom123"))
                .andExpect(jsonPath("$.data.enabled").value(false))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    void testUpdateNotFound() throws Exception{
        UserDto userDto = new UserDto(3, "michael", false, "user");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(3);
        userEntity.setUsername("michael");
        userEntity.setEnabled(false);
        userEntity.setRoles("user");

        String json = this.objectMapper.writeValueAsString(userDto);

        // Given
        given(this.userService.update(eq(3), Mockito.any(UserEntity.class))).willThrow(new ObjectNotFoundException("user", 3));

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id: 3"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteSuccess() throws Exception{
        // Given
        doNothing().when(this.userService).delete(2);

        // When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete User Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteErrorWithNonExistentUserId() throws Exception{
        // Given
        doThrow(new ObjectNotFoundException("user", 2)).when(this.userService).delete(2);

        // When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id: 2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
