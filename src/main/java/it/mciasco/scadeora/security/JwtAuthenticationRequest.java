package it.mciasco.scadeora.security;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtAuthenticationRequest {

    private String username;
    private String password;

}
