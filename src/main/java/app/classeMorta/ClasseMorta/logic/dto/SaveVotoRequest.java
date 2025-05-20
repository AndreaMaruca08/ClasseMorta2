package app.classeMorta.ClasseMorta.logic.dto;

import app.classeMorta.ClasseMorta.logic.PeriodoVoto;

public record SaveVotoRequest(float voto, Long idMateria, Long idStudente, PeriodoVoto periodoVoto) {
}
