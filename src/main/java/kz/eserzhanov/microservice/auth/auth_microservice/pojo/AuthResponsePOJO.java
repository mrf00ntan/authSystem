package kz.eserzhanov.microservice.auth.auth_microservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponsePOJO {
    private String accessToken;
    private String refreshToken;
}
