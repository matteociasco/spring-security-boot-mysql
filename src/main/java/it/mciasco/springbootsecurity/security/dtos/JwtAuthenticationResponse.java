package it.mciasco.scadeora.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class JwtAuthenticationResponse implements Serializable {

    private String username;
    private String token;
    private List<String> authorities;

}
