package br.com.clinica.ms_atendimento.prontuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class EvolucaoClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long prontuarioId;

    @NotNull
    private Long atendimentoId;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String subjetivo;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String objetivo;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String avaliacao;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String plano;

    private LocalDateTime dataRegistro;
}