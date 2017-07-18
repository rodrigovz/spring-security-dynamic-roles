package py.com.example.controllers

import javax.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import py.com.example.services.RoleService

@Controller
class MainController {

	@Autowired
	RoleService roleService
	
	@RequestMapping(value = "/login")
	String login() {
		return "login"
	}
	
	@RequestMapping(value = ["", "/"])
	String index() {
		return "index"
	}
	
	@RequestMapping(value = "/org-a")
	String orgA(HttpServletRequest request) {
		request.getSession().setAttribute("org", "A")
		reloadRolesForAuthenticatedUser("A")
		return "redirect:/home"
	}
	
	@RequestMapping(value = "/org-b")
	String orgB(HttpServletRequest request) {
		request.getSession().setAttribute("org", "B")
		reloadRolesForAuthenticatedUser("B")
		return "redirect:/home"
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/home")
	String home(HttpServletRequest request, Model model) {
		model.addAttribute("org", request.getSession().getAttribute("org"))
		return "home"
	}
	
	//
	// Private
	//
	
	private void reloadRolesForAuthenticatedUser(String org) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication()
		List<String> newRoles = roleService.getRoles(auth.getPrincipal().getUsername(), org)
		List<GrantedAuthority> authorities = getAuthorities(newRoles)
		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials(),authorities)
		SecurityContextHolder.getContext().setAuthentication(newAuth)
	}
	
	
	private List<GrantedAuthority> getAuthorities(List<String> roles) {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>()

		if (!roles.isEmpty()) {
			for (String r : roles) {
				auths.add(new SimpleGrantedAuthority(r))
			}
		}

		return auths
	}
}
