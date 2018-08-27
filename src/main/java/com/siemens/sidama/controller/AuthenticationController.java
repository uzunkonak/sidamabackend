package com.siemens.sidama.controller;

import com.siemens.sidama.entity.User;
import com.siemens.sidama.service.UserService;
import com.siemens.sidama.util.AuthToken;
import com.siemens.sidama.util.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("token")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Request method to create a new account by a guest", response = User.class)
    @CrossOrigin
    @RequestMapping(value = "/generate-token", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody User loginUser) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final User user = userService.find(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);

        user.setToken(token);
        return ResponseEntity.ok(user);
    }
}
