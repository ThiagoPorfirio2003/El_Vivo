package com.porfirio.elvivo.features.auth;

import com.porfirio.elvivo.domain.user.credential.UserCredentialDetailsService;
import com.porfirio.elvivo.domain.user.credential.UserCredentialRepository;
import com.porfirio.elvivo.security.jwt.JwtAuthenticationFilter;
import com.porfirio.elvivo.security.jwt.JwtAuthenticationProvider;
import com.porfirio.elvivo.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
public class AuthConfig
{
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /*
    @Bean(name = "usernamePasswordAuthProvider")
    public AuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
    {
        var authenticationProvider = new DaoAuthenticationProvider(passwordEncoder);

        authenticationProvider.setUserDetailsService(userDetailsService);

        return authenticationProvider;
    }
    */

        /*
    @Bean(name = "usernamePasswordAuthManager")
    public AuthenticationManager usernamePasswordAuthManager(
            @Qualifier("usernamePasswordAuthProvider")
            AuthenticationProvider usernamePasswordAuthProvider)
    {
        return new ProviderManager(List.of(usernamePasswordAuthProvider));
    }*/

    /*
    @Bean(name = "jwtAuthProvider")
    public AuthenticationProvider jwtAuthenticationProvider(JwtService jwtService)
    {
        return new JwtAuthenticationProvider(jwtService);
    }
    */

    @Bean(name = "jwtAuthManager")
    public AuthenticationManager jwtProviderManager(
             @Qualifier("jwtAuthProvider")
             AuthenticationProvider jwtAuthenticationProvider)
    {
        return new ProviderManager(List.of(jwtAuthenticationProvider));
    }

    @Bean(name = "usernamePasswordAuthManager")
    public AuthenticationManager usernamePasswordAuthManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
    {
        var authenticationProvider = new DaoAuthenticationProvider(passwordEncoder);

        authenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(List.of(authenticationProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception
    {
        http
                .authorizeHttpRequests(request->
                    request
                            .requestMatchers("/auth/login", "/auth/register").permitAll()
                            .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManeger->
                        sessionManeger.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
