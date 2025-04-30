package com.parent.AdministrationSystem.security;

import com.parent.AdministrationSystem.entity.Users;
import com.parent.AdministrationSystem.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
//UserDetailsService is an inbuilt interface
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;


    public UserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    //loadUserByUsername generally requires an username but here I am passing email as they unique as well
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = (Users) usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with the given email: " + email));

        return new CustomUserDetails(user);
    }

}