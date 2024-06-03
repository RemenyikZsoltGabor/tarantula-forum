package hu.remzso.tarantulaForum.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import hu.remzso.tarantulaForum.security.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//http.csrf().disable().authorizeRequests()
			//	.antMatchers("/index").permitAll()
			//	.antMatchers("/login").permitAll()
			//	.anyRequest().authenticated()
			
		//return http.build();
		http.csrf().disable()
		.authorizeHttpRequests(authorize -> authorize
				.antMatchers("/index", "/tarantulas", "/info",  "/registerUser", "/registerAddress", "/register", "/login", "/registrationError","/thanks").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(login -> login
					.loginPage("/index")
					.loginProcessingUrl("/login")
					.defaultSuccessUrl("/actualUser", true)
					.permitAll())
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/index")
					.permitAll());
		
		http.userDetailsService(userDetailsServiceImpl);
		return http.build();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {

		return NoOpPasswordEncoder.getInstance();
	}
}

