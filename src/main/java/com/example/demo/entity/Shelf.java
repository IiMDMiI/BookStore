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

@Table(name = "shelves")
public class Shelf {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "shelves")
    private List<Book> books;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
