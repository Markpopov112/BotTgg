package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeRequests) -> {
            ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)authorizeRequests.requestMatchers(HttpMethod.GET, new String[]{"/jokes", "/jokes/*"})).permitAll().requestMatchers(HttpMethod.POST, new String[]{"/jokes"})).authenticated().requestMatchers(HttpMethod.PUT, new String[]{"/jokes/*"})).hasRole("MODERATOR").requestMatchers(HttpMethod.DELETE, new String[]{"/jokes/*"})).hasRole("MODERATOR").requestMatchers(HttpMethod.GET, new String[]{"/register", "/registerView"})).permitAll().requestMatchers(HttpMethod.POST, new String[]{"/users/register"})).permitAll().requestMatchers(new String[]{"/login"})).permitAll().requestMatchers(new String[]{"/users/**"})).hasRole("ADMIN").anyRequest()).authenticated();
        }).formLogin((form) -> {
            ((FormLoginConfigurer)form.loginPage("/login").defaultSuccessUrl("/jokes", true)).permitAll();
        }).logout((logout) -> {
            logout.logoutUrl("/logout").logoutSuccessUrl("/login");
        }).csrf((csrf) -> {
            csrf.disable();
        });
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}