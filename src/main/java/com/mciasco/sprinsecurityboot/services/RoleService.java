package com.mciasco.springsecurityboot.services;

import com.mciasco.springsecurityboot.domain.Role;
import com.mciasco.springsecurityboot.persistence.RoleRepository;
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
