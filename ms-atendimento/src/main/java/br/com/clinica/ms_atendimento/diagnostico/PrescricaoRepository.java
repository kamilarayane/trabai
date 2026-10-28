package br.com.clinica.ms_atendimento.diagnostico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {

    List<Prescricao> findByAtendimentoId(Long atendimentoId);
}