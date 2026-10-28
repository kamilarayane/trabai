package br.com.clinica.ms_atendimento.atendimento;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AtendimentoService {

    private final AtendimentoRepository repository;

    public AtendimentoService(AtendimentoRepository repository) {
        this.repository = repository;
    }

    public Atendimento abrir(Atendimento atendimento) {
        if (repository.existsByConsultaId(atendimento.getConsultaId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um atendimento para essa consulta");
        }

        // Campos controlados pelo sistema: ignora o que veio no corpo da requisição
        atendimento.setId(null);
        atendimento.setInicio(LocalDateTime.now());
        atendimento.setFim(null);
        atendimento.setStatus(StatusAtendimento.EM_ANDAMENTO);

        try {
            return repository.save(atendimento);
        } catch (DataIntegrityViolationException e) {
            // duas requisições simultâneas para a mesma consulta (constraint única)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um atendimento para essa consulta");
        }
    }

    public Atendimento buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Atendimento não encontrado"));
    }

    /**
     * Busca o atendimento e garante que ainda está aberto.
     * Usado antes de registrar diagnóstico, exame, prescrição ou evolução.
     */
    public Atendimento buscarEmAndamento(Long id) {
        Atendimento atendimento = buscar(id);
        if (atendimento.getStatus() == StatusAtendimento.FINALIZADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Atendimento já finalizado");
        }
        return atendimento;
    }

    public List<Atendimento> listarTodos() {
        return repository.findAll();
    }

    public List<Atendimento> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    public Atendimento finalizar(Long id) {
        Atendimento atendimento = buscar(id);

        if (atendimento.getStatus() == StatusAtendimento.FINALIZADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Atendimento já finalizado");
        }

        atendimento.setFim(LocalDateTime.now());
        atendimento.setStatus(StatusAtendimento.FINALIZADO);
        return repository.save(atendimento);
    }
}
