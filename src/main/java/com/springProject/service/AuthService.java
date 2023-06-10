package com.springProject.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springProject.dto.RegisterRequest;
import com.springProject.exception.SpringRedditException;
import com.springProject.model.User;
import com.springProject.model.VerificationToken;
import com.springProject.repository.UserRepository;
import com.springProject.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static java.time.Instant.now;

import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository verificationTokenRepository;
	@Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(now());
        user.setEnabled(false);

        userRepository.save(user);
    }
	private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }
	 public void verifyAccount(String token) {
	        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
	        verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
	        fetchUserAndEnable(verificationTokenOptional.get());
	    }

	    @Transactional
	    private void fetchUserAndEnable(VerificationToken verificationToken) {
	        String username = verificationToken.getUser().getUsername();
	        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User Not Found with id - " + username));
	        user.setEnabled(true);
	        userRepository.save(user);
	    }
	private String encodePassword(String password) {
		 return passwordEncoder.encode(password);
	}
}
