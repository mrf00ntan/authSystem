package kz.eserzhanov.microservice.auth.auth_microservice.config;

import kz.eserzhanov.microservice.auth.auth_microservice.config.jwt.JWTAuthorizationFilter;
import kz.eserzhanov.microservice.auth.auth_microservice.controller.exception.SelfAccessDeniedHandler;
import kz.eserzhanov.microservice.auth.auth_microservice.controller.exception.SelfAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    private final String AUTH_ENDPOINT = "/api/auth/**";
    private final String ACTUATOR_ENDPOINT = "/actuator/**";

    public WebSecurityConfig(JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new SelfAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new SelfAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable();
        http.csrf().disable(); // TODO: keep only in dev
        http.addFilterAfter(jwtAuthorizationFilter, BasicAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers(AUTH_ENDPOINT).anonymous()
                .antMatchers(ACTUATOR_ENDPOINT).permitAll()
                .anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
            .authenticationEntryPoint(authenticationEntryPoint());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
