package br.com.clinica.ms_atendimento.diagnostico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoExameRepository extends JpaRepository<SolicitacaoExame, Long> {

    List<SolicitacaoExame> findByAtendimentoId(Long atendimentoId);
}