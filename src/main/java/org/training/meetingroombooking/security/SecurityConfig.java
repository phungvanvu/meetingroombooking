package org.training.meetingroombooking.security;

public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.();
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
