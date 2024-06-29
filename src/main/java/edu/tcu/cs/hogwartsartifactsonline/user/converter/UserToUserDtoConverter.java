package edu.tcu.cs.hogwartsartifactsonline.user.converter;

import edu.tcu.cs.hogwartsartifactsonline.user.UserEntity;
import edu.tcu.cs.hogwartsartifactsonline.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<UserEntity, UserDto> {

    @Override
    public UserDto convert(UserEntity source) {
        // We cannot set password in DTO
        return new UserDto(source.getId(),
                source.getUsername(),
                source.isEnabled(),
                source.getRoles());
    }
}
