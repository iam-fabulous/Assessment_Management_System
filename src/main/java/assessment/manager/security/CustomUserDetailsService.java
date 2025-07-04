package assessment.manager.security;

import assessment.manager.data.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map( user ->  AuthenticatedUser.builder()
                        .user(user)
                        .build())
                .orElseThrow(()-> new UsernameNotFoundException( "User with the email %s not found".formatted( email )));
    }
}
