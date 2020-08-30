package it.mciasco.scadeora.domain;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PUBLIC)
    private Long id;

    private String username;
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PUBLIC)
    private String password;

    private boolean enabled;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "ID")})
    private List<Role> authorities;

    public User(String subject, String username, List<SimpleGrantedAuthority> authorities, boolean isEnabled) {
    }

    public void addRole(Role role) {
        if (role != null) {
            this.authorities.add(role);
        }
    }

    public void removeRole(Role role) {
        if (role != null) {
            this.authorities.remove(role);
        }
    }

    // TODO
//    public UserDto toDto() {
//        return UserDto.builder()
//                .username(this.username)
//                .authorities(this.authorities.stream().map(Role::getAuthority).collect(Collectors.toList()))
//                .build();
//
//    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


}
