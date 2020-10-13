package me.hoseok.twitterdemo.test;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity root() {
        return ResponseEntity.ok("Welcome");
    }

    @GetMapping("/admin")
    public ResponseEntity admin() {
        return ResponseEntity.ok("admin");
    }

    @GetMapping("/api/accounts/test")
    public ResponseEntity test() {
        return ResponseEntity.ok("test");

    }

    @GetMapping("/user/{username}")
    public ResponseEntity user(@PathVariable String username, Principal principal) {
        System.out.println("username : " + principal.getName());
        return ResponseEntity.ok(username);
    }
}
