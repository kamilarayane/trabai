package br.com.clinica.ms_atendimento.prontuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvolucaoClinicaRepository extends JpaRepository<EvolucaoClinica, Long> {

    List<EvolucaoClinica> findByProntuarioIdOrderByDataRegistroDesc(Long prontuarioId);
}