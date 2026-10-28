package br.com.clinica.ms_atendimento.prontuario;

import br.com.clinica.ms_atendimento.atendimento.Atendimento;
import br.com.clinica.ms_atendimento.atendimento.AtendimentoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final EvolucaoClinicaRepository evolucaoRepository;
    private final AtendimentoService atendimentoService;

    public ProntuarioService(ProntuarioRepository prontuarioRepository,
                             EvolucaoClinicaRepository evolucaoRepository,
                             AtendimentoService atendimentoService) {
        this.prontuarioRepository = prontuarioRepository;
        this.evolucaoRepository = evolucaoRepository;
        this.atendimentoService = atendimentoService;
    }

    public Prontuario criar(Prontuario prontuario) {
        if (prontuarioRepository.existsByPacienteId(prontuario.getPacienteId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esse paciente já possui prontuário");
        }
        prontuario.setId(null);
        try {
            return prontuarioRepository.save(prontuario);
        } catch (DataIntegrityViolationException e) {
            // requisições simultâneas para o mesmo paciente (constraint única)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esse paciente já possui prontuário");
        }
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

    /**
     * Atualização parcial: só altera os campos enviados (não nulos).
     * Campo ausente no corpo é mantido como está, para não apagar dados clínicos
     * (ex.: alergias) sem querer. Para limpar um campo, envie "" (string vazia).
     */
    public Prontuario atualizar(Long id, Prontuario dados) {
        Prontuario prontuario = buscar(id);

        if (dados.getAlergias() != null) {
            prontuario.setAlergias(dados.getAlergias());
        }
        if (dados.getMedicamentosEmUso() != null) {
            prontuario.setMedicamentosEmUso(dados.getMedicamentosEmUso());
        }
        if (dados.getDoencasPreExistentes() != null) {
            prontuario.setDoencasPreExistentes(dados.getDoencasPreExistentes());
        }
        if (dados.getHistoricoFamiliar() != null) {
            prontuario.setHistoricoFamiliar(dados.getHistoricoFamiliar());
        }
        return prontuarioRepository.save(prontuario);
    }

    public EvolucaoClinica registrarEvolucao(Long prontuarioId, EvolucaoClinica evolucao) {
        Prontuario prontuario = buscar(prontuarioId);
        Atendimento atendimento = atendimentoService.buscarEmAndamento(evolucao.getAtendimentoId());

        if (!atendimento.getPacienteId().equals(prontuario.getPacienteId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esse atendimento não pertence ao paciente do prontuário");
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
