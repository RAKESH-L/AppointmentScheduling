package com.springrest.appointment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
		.inMemoryAuthentication()
		.passwordEncoder(getPasswordEncoder())
		.withUser("rakesh@gmail.com").password(getPasswordEncoder().encode("rakesh@12")).authorities("STUDENT")
		.and()
		.withUser("harry@gmail.com").password(getPasswordEncoder().encode("harry@12")).authorities("ADMIN")
		.and()
		.withUser("lokesh@gmail.com").password(getPasswordEncoder().encode("lokesh@12")).authorities("STUDENT");
	
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET,"/api/user/login").authenticated()
		.antMatchers(HttpMethod.POST,"/api/course/add").hasAuthority("ADMIN")
		.antMatchers(HttpMethod.GET,"/api/course/all").hasAuthority("ADMIN")
		.antMatchers(HttpMethod.GET,"/api/course/trainer/all").hasAuthority("STUDENT")
		.antMatchers(HttpMethod.POST,"/api/slot/add/{id}").hasAuthority("ADMIN")
		.antMatchers(HttpMethod.GET,"/api/slot/course/{cid}").hasAuthority("STUDENT")
		.antMatchers(HttpMethod.GET,"/api/appointment/all").hasAuthority("STUDENT")
		.antMatchers(HttpMethod.POST,"/api/appointment/add/{cid}").hasAuthority("STUDENT")
		.antMatchers(HttpMethod.PUT,"/api/appointment/status/{status}/{id}").hasAuthority("ADMIN")
		.antMatchers(HttpMethod.GET,"/api/appointment/all/{status}").hasAuthority("ADMIN")
		.anyRequest().permitAll()
		.and()
		.httpBasic()
		.and()
		.csrf().disable();
	}
	
	//that allows you to autowire to PasswordEncoder from anywhere in the APP
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
