package ru.kata.spring.boot_security.demo.service;


import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
   private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                            @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserById(Long id){
        User user = null;
        Optional<User> userFromBD = userRepository.findById(id);
        if (userFromBD.isPresent()) {
            user = userFromBD.get();
        }
        return user;
    }


    @Override
    @Transactional(readOnly = true)
    public List<User> allUsers() {
        return userRepository.findAll();
    }
    @Override
    @Transactional
    public User findByUsername (String username) {
        return userRepository.findByUsername(username).get();
    }
    @Override
    @Transactional
    public void saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void update(User user, Long id) {
        User userFromDb = userRepository.findById(id).get();
        if (userFromDb.getPassword().equals(user.getPassword())) {
            userRepository.save(user);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findByUsername(username);
//        return user.orElseThrow(() -> new UsernameNotFoundException(String.format("User" + username + "not found")));
//    }
}
