package app.classeMorta.ClasseMorta.logic.dto;

import app.classeMorta.ClasseMorta.logic.PeriodoVoto;

public record MediaRequest(Long idMateria, Long idStudente, float ipotetico, PeriodoVoto periodoVoto) {
}
