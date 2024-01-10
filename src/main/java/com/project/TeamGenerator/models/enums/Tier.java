package com.project.TeamGenerator.models.enums;

import lombok.Getter;

@Getter
public enum Tier {
    S(16),A(8),B(4),C(2),D(1),E(0);

    private final int value;

    Tier(int value) {
        this.value = value;
    }

    public static Tier getTierByValue(int value){
        return switch (value) {
            case 16 -> Tier.S;
            case 8 -> Tier.A;
            case 4 -> Tier.B;
            case 2 -> Tier.C;
            case 1 -> Tier.D;
            default -> Tier.E;
        };
    }

    public static int getMaxValue(){return 16;}

}
