package it.mciasco.scadeora.security.dtos;

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
