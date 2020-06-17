package kz.eserzhanov.microservice.auth.auth_microservice.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "dir___refresh_token")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshToken extends BaseEntity{
    @Column(name = "refresh_token", unique = true, nullable = false)
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public RefreshToken(String refreshToken, User user){
        this.refreshToken = refreshToken;
        this.user = user;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @CreatedDate
    private Date created;

    @PrePersist
    public void setPersist() {
        this.created = new Date();
    }
}
