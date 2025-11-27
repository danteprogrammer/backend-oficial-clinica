package com.saludvida.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authRequest -> authRequest
                                                .requestMatchers("/api/public/**").permitAll()
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .requestMatchers("/error").permitAll()
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers("/api/pacientes/activos")
                                                .hasAnyAuthority("RECEPCIONISTA", "TRIAJE", "MEDICO")
                                                .requestMatchers(HttpMethod.GET, "/api/medicos/especialidades")
                                                .hasAuthority("RECEPCIONISTA")
                                                .requestMatchers(HttpMethod.GET, "/api/medicos/especialidad/**")
                                                .hasAuthority("RECEPCIONISTA")
                                                .requestMatchers(HttpMethod.GET, "/api/medicos/*/horario")
                                                .hasAuthority("RECEPCIONISTA")
                                                .requestMatchers("/api/historias/paciente/**")
                                                .hasAnyAuthority("RECEPCIONISTA", "MEDICO", "TRIAJE")
                                                .requestMatchers("/api/pacientes/**").hasAuthority("RECEPCIONISTA")
                                                .requestMatchers("/api/citas/**")
                                                .hasAnyAuthority("RECEPCIONISTA", "ADMIN")
                                                .requestMatchers("/api/turnos/**").hasAuthority("RECEPCIONISTA")
                                                .requestMatchers(HttpMethod.GET, "/api/facturacion/citas-pendientes/**")
                                                .hasAuthority("CAJA")
                                                .requestMatchers(HttpMethod.PUT, "/api/facturacion/registrar-pago/**")
                                                .hasAuthority("CAJA")
                                                .requestMatchers(HttpMethod.GET, "/api/facturacion/citas-pagadas")
                                                .hasAuthority("CAJA")
                                                .requestMatchers("/api/seguros/**").hasAuthority("CAJA")
                                                .requestMatchers("/api/triajes/**").hasAnyAuthority("TRIAJE", "MEDICO")
                                                .requestMatchers(HttpMethod.POST, "/api/consultas/historia/**")
                                                .hasAuthority("MEDICO")
                                                .requestMatchers(HttpMethod.GET, "/api/consultas/historial/**")
                                                .hasAuthority("MEDICO")
                                                .requestMatchers("/api/laboratorio/ordenar").hasAuthority("MEDICO")
                                                .requestMatchers("/api/laboratorio/historia/**").hasAuthority("MEDICO")
                                                .requestMatchers("/api/laboratorio/pendientes")
                                                .hasAuthority("LABORATORIO")
                                                .requestMatchers("/api/laboratorio/completadas")
                                                .hasAuthority("LABORATORIO")
                                                .requestMatchers("/api/laboratorio/*/estado")
                                                .hasAuthority("LABORATORIO")
                                                .requestMatchers("/api/laboratorio/*/resultados")
                                                .hasAuthority("LABORATORIO")
                                                .requestMatchers("/api/dashboard/**").hasAuthority("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/tarifario")
                                                .hasAnyAuthority("ADMIN", "CAJA")
                                                .requestMatchers("/api/tarifario/**").hasAuthority("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/medicos/{id}/inactivar")
                                                .hasAuthority("ADMIN")
                                                .requestMatchers("/api/admin/usuarios/**").hasAuthority("ADMIN")
                                                .requestMatchers("/api/admin/horarios/**").hasAuthority("ADMIN")
                                                .requestMatchers("/api/admin/especialidades/**").hasAuthority("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/consultorios",
                                                                "/api/consultorios/**")
                                                .hasAnyAuthority("ADMIN", "RECEPCIONISTA", "MEDICO", "TRIAJE")
                                                .requestMatchers(HttpMethod.POST, "/api/consultorios")
                                                .hasAuthority("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/consultorios/**")
                                                .hasAuthority("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/consultorios/**")
                                                .hasAuthority("ADMIN")
                                                .requestMatchers("/api/medicos/**").hasAuthority("ADMIN")
                                                .anyRequest().authenticated())
                                .sessionManagement(sessionManager -> sessionManager
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList(
                                "http://localhost:4200",
                                "https://frontend-oficial-clinica-3lrb.vercel.app"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}