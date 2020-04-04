package com.mciasco.springsecurityboot.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority, Serializable {

    @Transient public static long ROLE_ADMIN = 1L;
    @Transient public static long ROLE_USER = 2L;

    @Id
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private Long id;

    private String authority;

}
