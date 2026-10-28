package br.com.clinica.ms_atendimento.diagnostico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SolicitacaoExame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long atendimentoId;

    @NotBlank
    private String codigoTuss;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String descricao;

    private boolean urgente;

    @Enumerated(EnumType.STRING)
    private StatusExame status;
}