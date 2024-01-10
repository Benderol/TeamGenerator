package com.project.TeamGenerator.models;

import com.project.TeamGenerator.models.enums.Tier;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "player")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private Tier tier;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean isPlaying = true;
}
