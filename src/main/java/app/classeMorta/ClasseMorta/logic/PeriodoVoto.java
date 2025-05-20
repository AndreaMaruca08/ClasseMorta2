package app.classeMorta.ClasseMorta.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PeriodoVoto {
    TRIMESTRE(1),
    PENTAMESTRE(2),
    ANNO(3);

    private final int codice;
}