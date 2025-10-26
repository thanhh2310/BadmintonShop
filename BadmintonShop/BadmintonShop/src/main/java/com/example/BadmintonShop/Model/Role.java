package com.example.BadmintonShop.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String roleName;

    @Override
    public String getAuthority() {
        return this.roleName;
    }
}
