package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.LoginRequestDTO;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.UMapper;
import com.codewithmosh.store.repositories.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name="Auth")
public class AuthController {

//    private final UserRepository userRepository;
//    private final UMapper userMapper;
//    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    };


    @PostMapping("/login")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody LoginRequestDTO request,
            UriComponentsBuilder uriComponentsBuilder
    ){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        return ResponseEntity.ok().build();

//        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
//        if(user == null){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
//                    Map.of("error", "Invalid credentials")
//            );
//        }
//        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        };
//        return ResponseEntity.ok().build();
    };
}
