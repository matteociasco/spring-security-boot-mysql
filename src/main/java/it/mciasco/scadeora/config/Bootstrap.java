package it.mciasco.scadeora.config;

import it.mciasco.scadeora.domain.Role;
import it.mciasco.scadeora.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class Bootstrap {

    @Autowired
    RoleService roleService;

    @PostConstruct
    private void init() {
        if(roleService.findByAuthority("ROLE_ADMIN") == null){
            Role adminRole = Role.builder()
                    .id(Role.ROLE_ADMIN)
                    .authority("ROLE_ADMIN")
                    .build();

            roleService.save(adminRole);
        }

        if(roleService.findByAuthority("ROLE_USER") == null){
            Role userRole = Role.builder()
                    .id(Role.ROLE_USER)
                    .authority("ROLE_USER")
                    .build();

            roleService.save(userRole);
        }
    }

}
