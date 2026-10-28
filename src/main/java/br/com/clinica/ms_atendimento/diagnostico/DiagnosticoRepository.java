package br.com.clinica.ms_atendimento.diagnostico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    List<Diagnostico> findByAtendimentoId(Long atendimentoId);
}