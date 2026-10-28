package br.com.clinica.ms_atendimento.diagnostico;

import jakarta.persistence.Entity;
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
public class Prescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long atendimentoId;

    @NotBlank
    private String medicamento;

    @NotBlank
    private String dosagem;

    @NotBlank
    private String posologia;

    private String duracao;
}