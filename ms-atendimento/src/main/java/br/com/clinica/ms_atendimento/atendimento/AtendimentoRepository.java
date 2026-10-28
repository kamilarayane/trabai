
package br.com.clinica.ms_atendimento.atendimento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    List<Atendimento> findByPacienteId(Long pacienteId);
    boolean existsByConsultaId(Long consultaId);
}