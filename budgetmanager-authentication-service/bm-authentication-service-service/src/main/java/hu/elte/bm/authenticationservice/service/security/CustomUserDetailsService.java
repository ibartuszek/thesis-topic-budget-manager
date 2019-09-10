package hu.elte.bm.authenticationservice.service.security;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import hu.elte.bm.authenticationservice.dal.UserDao;
import hu.elte.bm.authenticationservice.domain.User;
import hu.elte.bm.authenticationservice.domain.UserCannotBeFoundException;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${user.user_cannot_be_found:User cannot be found in the repository!}")
    private String userCannotBeFound;

    private UserDao userDao;

    CustomUserDetailsService(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByEmailWithPassword(userName);
        if (user.isEmpty()) {
            throw new UserCannotBeFoundException(userCannotBeFound);
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), new ArrayList<>());
    }

}
