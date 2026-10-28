package br.com.clinica.ms_atendimento.diagnostico;

import br.com.clinica.ms_atendimento.atendimento.AtendimentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final SolicitacaoExameRepository exameRepository;
    private final PrescricaoRepository prescricaoRepository;
    private final AtendimentoRepository atendimentoRepository;

    public DiagnosticoService(DiagnosticoRepository diagnosticoRepository,
                              SolicitacaoExameRepository exameRepository,
                              PrescricaoRepository prescricaoRepository,
                              AtendimentoRepository atendimentoRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.exameRepository = exameRepository;
        this.prescricaoRepository = prescricaoRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    private void conferirAtendimento(Long atendimentoId) {
        if (!atendimentoRepository.existsById(atendimentoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atendimento não encontrado");
        }
    }

    // ---------- Diagnóstico ----------

    public Diagnostico registrarDiagnostico(Diagnostico diagnostico) {
        conferirAtendimento(diagnostico.getAtendimentoId());
        diagnostico.setId(null);
        return diagnosticoRepository.save(diagnostico);
    }

    public Diagnostico buscarDiagnostico(Long id) {
        return diagnosticoRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico não encontrado"));
    }

    public List<Diagnostico> listarDiagnosticos(Long atendimentoId) {
        conferirAtendimento(atendimentoId);
        return diagnosticoRepository.findByAtendimentoId(atendimentoId);
    }

    // ---------- Exames ----------

    public SolicitacaoExame solicitarExame(SolicitacaoExame exame) {
        conferirAtendimento(exame.getAtendimentoId());
        exame.setId(null);
        exame.setStatus(StatusExame.SOLICITADO);
        return exameRepository.save(exame);
    }

    public List<SolicitacaoExame> listarExames(Long atendimentoId) {
        conferirAtendimento(atendimentoId);
        return exameRepository.findByAtendimentoId(atendimentoId);
    }

    public SolicitacaoExame cancelarExame(Long id) {
        SolicitacaoExame exame = exameRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Exame não encontrado"));

        if (exame.getStatus() != StatusExame.SOLICITADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só dá para cancelar exame que está como solicitado");
        }

        exame.setStatus(StatusExame.CANCELADO);
        return exameRepository.save(exame);
    }

    // ---------- Prescrições ----------

    public Prescricao emitirPrescricao(Prescricao prescricao) {
        conferirAtendimento(prescricao.getAtendimentoId());
        prescricao.setId(null);
        return prescricaoRepository.save(prescricao);
    }

    public List<Prescricao> listarPrescricoes(Long atendimentoId) {
        conferirAtendimento(atendimentoId);
        return prescricaoRepository.findByAtendimentoId(atendimentoId);
    }
}