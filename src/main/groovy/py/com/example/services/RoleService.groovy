package py.com.example.services

import groovy.transform.CompileStatic

import org.springframework.stereotype.Service

@Service
@CompileStatic
class RoleService {

	List<String> getRoles(String user, String org) {
		if (user == "user1") {
			if (org == "A") {
				return ["ROLE_ADMIN", "ROLE_USER"]
			} else {
				return ["ROLE_USER"]
			}
		} else if (user == "user2") {
			if (org == "A") {
				return ["ROLE_USER"]
			} else {
				return ["ROLE_ADMIN", "ROLE_USER"]
			}
		}
		return []
	}
}
