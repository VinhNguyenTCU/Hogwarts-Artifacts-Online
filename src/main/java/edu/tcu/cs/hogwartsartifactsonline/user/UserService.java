package edu.tcu.cs.hogwartsartifactsonline.user;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> findAll() {
        return this.userRepository.findAll();
    }

    public UserEntity findUserById(Integer id){
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("user", id));
    }

    public UserEntity save(UserEntity user) {
        // We need to encode plain text password before saving to the DB!
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public UserEntity update(Integer userId, UserEntity updateContent) {
        UserEntity oldUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));

        oldUser.setUsername(updateContent.getUsername());
        oldUser.setEnabled(updateContent.isEnabled());
        oldUser.setRoles(updateContent.getRoles());

        return this.userRepository.save(oldUser);
    }

    public void delete(Integer userId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)// First, we need to find this user from database
                .map(userEntity -> new MyUserPrincipal(userEntity)) // If found, wrap the returned user instance in a MyUserPrincipal
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " is not found.")); // Otherwise, throw an exception
    }
}
