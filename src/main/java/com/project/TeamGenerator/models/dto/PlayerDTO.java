package com.project.TeamGenerator.models.dto;

import com.project.TeamGenerator.models.enums.Tier;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {
    private int id;
    private String firstName;
    private String lastName;
    private Tier tier;
    private boolean isPlaying;
}
