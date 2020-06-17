package kz.eserzhanov.microservice.auth.auth_microservice.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;
}
