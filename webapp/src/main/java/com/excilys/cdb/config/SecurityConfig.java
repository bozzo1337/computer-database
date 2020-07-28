package com.excilys.cdb.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.excilys.cdb.connector.MyDataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyDataSource dataSource;

	@Bean
	public DigestAuthenticationFilter digestAuthenticationFilter() throws Exception {
		DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
		digestAuthenticationFilter.setUserDetailsService(userDetailsServiceBean());
		digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());
		digestAuthenticationFilter.setPasswordAlreadyEncoded(false);
		return digestAuthenticationFilter;
	}

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.deleteUser("user");
		jdbcUserDetailsManager.createUser(
				User.builder().username("user").password("user").disabled(false).authorities("ROLE_USER").build());
		return jdbcUserDetailsManager;
	}

	@Bean
	public DigestAuthenticationEntryPoint digestEntryPoint() {
		DigestAuthenticationEntryPoint entryPoint = new DigestAuthenticationEntryPoint();
		entryPoint.setRealmName("CDB Realm");
		entryPoint.setKey("quarante-deux");
		return entryPoint;
	}

	@Bean
	public DaoAuthenticationProvider authProvider() throws Exception {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsServiceBean());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		class CustomLogoutHandler implements LogoutHandler {

			@Override
			public void logout(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) {
				authentication.setAuthenticated(false);
				try {
					request.logout();
				} catch (ServletException e) {
					e.printStackTrace();
				}
			}

		}
		http.addFilter(digestAuthenticationFilter()).exceptionHandling().authenticationEntryPoint(digestEntryPoint())
				.and().authorizeRequests().antMatchers("/create*").authenticated();
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.addLogoutHandler(new CustomLogoutHandler()).logoutSuccessUrl("/").deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).clearAuthentication(true).permitAll();
	}
}
