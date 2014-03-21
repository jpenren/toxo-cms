package es.toxo.cms.common.authentication;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import es.toxo.cms.common.config.Configuration;

public class ConfigurationFileAuthenticationProvider implements  org.springframework.security.authentication.AuthenticationProvider{
	
	private List<String> users;
	private List<String> passwords;
	
	public ConfigurationFileAuthenticationProvider() {
		final String usersValue = Configuration.get("users");
		final String passwordsValue = Configuration.get("passwords");
		users = Arrays.asList(usersValue.replace(" ", "").split(","));
		passwords = Arrays.asList(passwordsValue.replace(" ", "").split(","));
	}
	
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		final String user = String.valueOf(auth.getPrincipal());
		final String encodedPassword = DigestUtils.sha256Hex(String.valueOf(auth.getCredentials()));
		if(users.contains(user)){
			final int position = users.indexOf(user);
			final String password = passwords.get(position);
			if(password.equals(encodedPassword)){
				final Collection<? extends GrantedAuthority> roles = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
				return new UsernamePasswordAuthenticationToken(user, "*", roles);
			}
		}
		
		throw new BadCredentialsException("Invalid username or password");
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}

}
