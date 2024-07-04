package edu.tcu.cs.hogwartsartifactsonline.security;

import edu.tcu.cs.hogwartsartifactsonline.user.MyUserPrincipal;
import edu.tcu.cs.hogwartsartifactsonline.user.UserEntity;
import edu.tcu.cs.hogwartsartifactsonline.user.converter.UserToUserDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.user.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // Create user info
        MyUserPrincipal myUserPrincipal = (MyUserPrincipal) authentication.getPrincipal();
        UserEntity userEntity = myUserPrincipal.getUserEntity();
        UserDto userDto = this.userToUserDtoConverter.convert(userEntity);

        // Create a JWT
        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
