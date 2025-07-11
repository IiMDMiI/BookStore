package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "user_id")
    private Long id;
    private String name;
    private String surname;
    @Column(unique = true)
    private String email;
    private String passHash;
    @Setter
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> books;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Shelf> shelves;

    @OneToOne(mappedBy = ("user"), cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter
    private VerificationToken verificationToken;
}
