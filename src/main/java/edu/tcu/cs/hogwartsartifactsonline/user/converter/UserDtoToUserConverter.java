package edu.tcu.cs.hogwartsartifactsonline.user.converter;

import edu.tcu.cs.hogwartsartifactsonline.user.UserEntity;
import edu.tcu.cs.hogwartsartifactsonline.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, UserEntity> {


    @Override
    public UserEntity convert(UserDto source) {
        UserEntity user = new UserEntity();
        user.setUsername(source.username());
        user.setEnabled(source.enabled());
        user.setRoles(source.roles());
        return user;
    }
}
