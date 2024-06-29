package edu.tcu.cs.hogwartsartifactsonline.user;

import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.user.converter.UserDtoToUserConverter;
import edu.tcu.cs.hogwartsartifactsonline.user.converter.UserToUserDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.user.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final UserToUserDtoConverter userToUserDtoConverter;

    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping
    public Result findAllUsers(){
        List<UserEntity> foundUsers = this.userService.findAll();
        // Convert foundUsers to a List of userDto
        List<UserDto> userDto = foundUsers.stream()
                .map(this.userToUserDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDto);
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId){
        UserEntity userEntity = this.userService.findUserById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(userEntity);
        return new Result(true, StatusCode. SUCCESS, "Find User By Id Success", userDto);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody UserEntity newUser){
        // Convert userDto to user and then add to wizardRepository
        UserEntity savedUser = this.userService.save(newUser);
        UserDto savedUserDto = this.userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add User Success", savedUserDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto updateUserDto){
        UserEntity update = this.userDtoToUserConverter.convert(updateUserDto);
        UserEntity updatedUser = this.userService.update(userId, update);
        UserDto returnedUserDto = this.userToUserDtoConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update User Success", returnedUserDto);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete User Success");
    }

}
