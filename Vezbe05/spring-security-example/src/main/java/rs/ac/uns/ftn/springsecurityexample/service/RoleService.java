package rs.ac.uns.ftn.springsecurityexample.service;

import java.util.List;

import rs.ac.uns.ftn.springsecurityexample.model.Role;

public interface RoleService {
	Role findById(Long id);
	List<Role> findByName(String name);
}
