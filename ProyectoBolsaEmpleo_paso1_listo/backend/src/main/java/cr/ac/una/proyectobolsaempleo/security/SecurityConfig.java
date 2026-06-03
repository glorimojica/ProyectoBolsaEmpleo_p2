package cr.ac.una.proyectobolsaempleo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/buscar",
                                "/buscar-puestos",
                                "/login",
                                "/registro/**",
                                "/assets/**",
                                "/css/**",
                                "/img/**",
                                "/uploads/**",
                                "/favicon.ico",
                                "/vite.svg"
                        ).permitAll()

                        .requestMatchers("/api/v1/public/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register/empresa").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register/oferente").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/me").authenticated()

                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/empresa/**").hasRole("EMPRESA")
                        .requestMatchers("/api/v1/oferente/**").hasRole("OFERENTE")

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/empresa/**").hasRole("EMPRESA")
                        .requestMatchers("/oferente/**").hasRole("OFERENTE")

                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())

                .formLogin(form -> form.disable())

                .logout(logout -> logout.disable())

                .httpBasic(httpBasic -> httpBasic.disable())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(customUserDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}