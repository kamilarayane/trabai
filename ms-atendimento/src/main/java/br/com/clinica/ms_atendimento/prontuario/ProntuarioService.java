package br.com.clinica.ms_atendimento.prontuario;

import br.com.clinica.ms_atendimento.atendimento.AtendimentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final EvolucaoClinicaRepository evolucaoRepository;
    private final AtendimentoRepository atendimentoRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository,
                             EvolucaoClinicaRepository evolucaoRepository,
                             AtendimentoRepository atendimentoRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.evolucaoRepository = evolucaoRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    public Prontuario criar(Prontuario prontuario) {
        if (prontuarioRepository.existsByPacienteId(prontuario.getPacienteId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esse paciente já possui prontuário");
        }
        prontuario.setId(null);
        return prontuarioRepository.save(prontuario);
    }

    public Prontuario buscar(Long id) {
        return prontuarioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Prontuário não encontrado"));
    }

    public Prontuario buscarPorPaciente(Long pacienteId) {
        return prontuarioRepository.findByPacienteId(pacienteId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Esse paciente não possui prontuário"));
    }

    public Prontuario atualizar(Long id, Prontuario dados) {
        Prontuario prontuario = buscar(id);
        prontuario.setAlergias(dados.getAlergias());
        prontuario.setMedicamentosEmUso(dados.getMedicamentosEmUso());
        prontuario.setDoencasPreExistentes(dados.getDoencasPreExistentes());
        prontuario.setHistoricoFamiliar(dados.getHistoricoFamiliar());
        return prontuarioRepository.save(prontuario);
    }

    public EvolucaoClinica registrarEvolucao(Long prontuarioId, EvolucaoClinica evolucao) {
        buscar(prontuarioId);

        if (!atendimentoRepository.existsById(evolucao.getAtendimentoId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atendimento não encontrado");
        }

        evolucao.setId(null);
        evolucao.setProntuarioId(prontuarioId);
        evolucao.setDataRegistro(LocalDateTime.now());
        return evolucaoRepository.save(evolucao);
    }

    public List<EvolucaoClinica> listarEvolucoes(Long prontuarioId) {
        buscar(prontuarioId);
        return evolucaoRepository.findByProntuarioIdOrderByDataRegistroDesc(prontuarioId);
    }
}