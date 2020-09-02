package it.mciasco.scadeora.security.services;

import it.mciasco.scadeora.security.domain.Role;
import it.mciasco.scadeora.security.persistence.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role findByAuthority(String authority){
        return roleRepository.findByAuthority(authority);
    }

    public Role getRole(Long role){
        return roleRepository.getOne(role);
    }

    public void save(Role r){
        if(r != null)
            roleRepository.saveAndFlush(r);
    }

}
