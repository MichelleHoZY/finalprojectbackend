package vttp2022.csf.finalprojectbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import vttp2022.csf.finalprojectbackend.Models.Users;
import vttp2022.csf.finalprojectbackend.Repositories.FoodFormRepository;
import vttp2022.csf.finalprojectbackend.Repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        final Users users = userRepository.findByUsername(username);
        if (users == null) {
            throw new UsernameNotFoundException(username);
        }
        // UserDetails user = User.withUsername(users.getUsername()).password(new BCryptPasswordEncoder().encode(users.getPassword())).authorities("USER").build();
        return users;
    }

    
}


