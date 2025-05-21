package it.uniroma3.siw.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;
@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
            .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        http
          .csrf().and().cors().disable()
          .authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET,
                 "/", "/index", "/login", "/register",
                 "/css/**", "/images/**", "/favicon.ico")
               .permitAll()
            .requestMatchers(HttpMethod.POST, "/register", "/login")
               .permitAll()
            .requestMatchers("/admin/**")
               .hasAuthority(ADMIN_ROLE)
            .anyRequest()
               .authenticated()
          .and()
            .formLogin()
              .loginPage("/login")
              .permitAll()
              .defaultSuccessUrl("/success", true)
              .failureUrl("/login?error=true")
          .and()
            .logout()
              .logoutUrl("/logout")
              .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
              .logoutSuccessUrl("/")
              .invalidateHttpSession(true)
              .deleteCookies("JSESSIONID")
              .clearAuthentication(true)
              .permitAll();
        return http.build();
    }

}
