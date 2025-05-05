package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.JwtResponseDTO;
import com.codewithmosh.store.dtos.LoginRequestDTO;
import com.codewithmosh.store.dtos.UserDTO;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.UMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name="Auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    };

    @PostMapping("/validate")
    public Boolean validate( @RequestHeader("Authorization") String token ){
        return jwtService.validateToken(token.substring(7));
    };

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(){
        System.out.println("@GetMapping ME");
        // Get security auth object
        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        var email = (String) authentication.getPrincipal();
//        var user = userRepository.findByEmail(email).orElse(null);
        var userID = (Long) authentication.getPrincipal();
        var user = userRepository.findById(userID).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userMapper.toUserDto(user));
        }
    };

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request,
            UriComponentsBuilder uriComponentsBuilder
    ){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        // Generate the token using a complete user object
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var token = jwtService.generateToken(user);
        return ResponseEntity.ok( new JwtResponseDTO(token));

        // Generate the token using a complete email
//        var token = jwtService.generateToken(request.getEmail());
//        return ResponseEntity.ok( new JwtResponseDTO(token));

    };
}



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