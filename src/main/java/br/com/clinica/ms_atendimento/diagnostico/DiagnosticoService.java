package br.com.clinica.ms_atendimento.diagnostico;

import br.com.clinica.ms_atendimento.atendimento.AtendimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final SolicitacaoExameRepository exameRepository;
    private final PrescricaoRepository prescricaoRepository;
    private final AtendimentoService atendimentoService;

    public DiagnosticoService(DiagnosticoRepository diagnosticoRepository,
                              SolicitacaoExameRepository exameRepository,
                              PrescricaoRepository prescricaoRepository,
                              AtendimentoService atendimentoService) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.exameRepository = exameRepository;
        this.prescricaoRepository = prescricaoRepository;
        this.atendimentoService = atendimentoService;
    }

    // ---------- Diagnóstico ----------

    public Diagnostico registrarDiagnostico(Diagnostico diagnostico) {
        atendimentoService.buscarEmAndamento(diagnostico.getAtendimentoId());
        diagnostico.setId(null);
        return diagnosticoRepository.save(diagnostico);
    }

    public Diagnostico buscarDiagnostico(Long id) {
        return diagnosticoRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnóstico não encontrado"));
    }

    public List<Diagnostico> listarDiagnosticos(Long atendimentoId) {
        atendimentoService.buscar(atendimentoId);
        return diagnosticoRepository.findByAtendimentoId(atendimentoId);
    }

    // ---------- Exames ----------

    public SolicitacaoExame solicitarExame(SolicitacaoExame exame) {
        atendimentoService.buscarEmAndamento(exame.getAtendimentoId());
        exame.setId(null);
        exame.setStatus(StatusExame.SOLICITADO);
        return exameRepository.save(exame);
    }

    public List<SolicitacaoExame> listarExames(Long atendimentoId) {
        atendimentoService.buscar(atendimentoId);
        return exameRepository.findByAtendimentoId(atendimentoId);
    }

    // Cancelar e marcar como realizado continuam permitidos mesmo com o atendimento
    // finalizado: o resultado de um exame normalmente chega depois da consulta.

    public SolicitacaoExame cancelarExame(Long id) {
        SolicitacaoExame exame = buscarExameSolicitado(id, "cancelar");
        exame.setStatus(StatusExame.CANCELADO);
        return exameRepository.save(exame);
    }

    public SolicitacaoExame realizarExame(Long id) {
        SolicitacaoExame exame = buscarExameSolicitado(id, "marcar como realizado");
        exame.setStatus(StatusExame.REALIZADO);
        return exameRepository.save(exame);
    }

    private SolicitacaoExame buscarExameSolicitado(Long id, String acao) {
        SolicitacaoExame exame = exameRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Exame não encontrado"));

        if (exame.getStatus() != StatusExame.SOLICITADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só dá para " + acao + " exame que está como solicitado");
        }
        return exame;
    }

    // ---------- Prescrições ----------

    public Prescricao emitirPrescricao(Prescricao prescricao) {
        atendimentoService.buscarEmAndamento(prescricao.getAtendimentoId());
        prescricao.setId(null);
        return prescricaoRepository.save(prescricao);
    }

    public List<Prescricao> listarPrescricoes(Long atendimentoId) {
        atendimentoService.buscar(atendimentoId);
        return prescricaoRepository.findByAtendimentoId(atendimentoId);
    }
}
