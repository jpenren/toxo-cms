package es.toxo.cms.common.authentication;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class AuthenticationProvider implements  org.springframework.security.authentication.AuthenticationProvider{
	
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
			
		Collection<? extends GrantedAuthority> roles = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
		return new UsernamePasswordAuthenticationToken("logued-user", "*", roles);
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}

}
