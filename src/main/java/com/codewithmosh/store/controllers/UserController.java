package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ChangePasswordDTO;
import com.codewithmosh.store.dtos.UserUpdateDTO;
import com.codewithmosh.store.dtos.UserDTO;
import com.codewithmosh.store.dtos.UserRegisterDTO;
import com.codewithmosh.store.entities.Role;
import com.codewithmosh.store.mappers.UMapper;
import com.codewithmosh.store.repositories.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name="User")
public class UserController {

    private final UserRepository userRepository;
    private final UMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UserDTO> getAllUsers(
            @RequestParam(required = false, defaultValue = "") String sortBy
    ){
        if(!Set.of("name", "email").contains((sortBy))){
            sortBy = "name";
        };
        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toUserDto)
                .toList();
//                .map(user -> new UserDTO( user.getName(), user.getEmail(), user.getPassword()))
//                .toList();
    };

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getAllUsers(@PathVariable Long id){
        var user= userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userMapper.toUserDto(user));
            //return ResponseEntity.ok(new UserDTO( user.getName(), user.getEmail(), user.getPassword()));
        }

    };

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UserUpdateDTO request
    ) {
        var user = userRepository.findById(id).orElse(null);
        if(user ==null) {
            return ResponseEntity.notFound().build();
        } else {
            userMapper.updateUser(request,  user);
            userRepository.save(user);
            return ResponseEntity.ok(userMapper.toUserDto(user));
        }
    };

    @PostMapping
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegisterDTO request,
            UriComponentsBuilder uriComponentsBuilder
    ){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(
                    Map.of("email", "Select another email")
            );
        }
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        var userDto = userMapper.toUserDto(user);

        return ResponseEntity.status(201).body(userDto);
    };

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser( @PathVariable(name = "id") Long id) {
        var user = userRepository.findById(id).orElse(null);
        if(user ==null) {
            return ResponseEntity.notFound().build();
        } else {
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        }
    };

    @PostMapping("/{id}/change-pswd")
    public ResponseEntity<UserDTO> changePassword(
            @PathVariable(name = "id") Long id,
            @RequestBody ChangePasswordDTO request
    ){
        var user = userRepository.findById(id).orElse(null);
        if(user ==null) {
            return ResponseEntity.notFound().build();
        } else {
            if(! user.getPassword().equals(request.getOld_password())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else {
                user.setPassword(request.getNew_password());
                userRepository.save(user);
                return ResponseEntity.noContent().build();
            }
        }
    };

}
