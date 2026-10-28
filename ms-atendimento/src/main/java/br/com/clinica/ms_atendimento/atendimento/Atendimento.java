package br.com.clinica.ms_atendimento.atendimento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long consultaId;

    @NotNull
    private Long pacienteId;

    @NotNull
    private Long medicoId;

    private LocalDateTime inicio;

    private LocalDateTime fim;

    @Enumerated(EnumType.STRING)
    private StatusAtendimento status;
}