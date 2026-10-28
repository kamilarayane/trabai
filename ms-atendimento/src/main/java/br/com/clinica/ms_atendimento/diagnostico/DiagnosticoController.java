package br.com.clinica.ms_atendimento.diagnostico;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class DiagnosticoController {

    private final DiagnosticoService service;

    public DiagnosticoController(DiagnosticoService service) {
        this.service = service;
    }

    // ---------- Diagnóstico ----------

    @PostMapping("/diagnosticos")
    @ResponseStatus(HttpStatus.CREATED)
    public Diagnostico registrarDiagnostico(@Valid @RequestBody Diagnostico diagnostico) {
        return service.registrarDiagnostico(diagnostico);
    }

    @GetMapping("/diagnosticos/{id}")
    public Diagnostico buscarDiagnostico(@PathVariable Long id) {
        return service.buscarDiagnostico(id);
    }

    @GetMapping("/diagnosticos")
    public List<Diagnostico> listarDiagnosticos(@RequestParam Long atendimentoId) {
        return service.listarDiagnosticos(atendimentoId);
    }

    // ---------- Exames ----------

    @PostMapping("/exames")
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitacaoExame solicitarExame(@Valid @RequestBody SolicitacaoExame exame) {
        return service.solicitarExame(exame);
    }

    @GetMapping("/exames")
    public List<SolicitacaoExame> listarExames(@RequestParam Long atendimentoId) {
        return service.listarExames(atendimentoId);
    }

    @PatchMapping("/exames/{id}/cancelar")
    public SolicitacaoExame cancelarExame(@PathVariable Long id) {
        return service.cancelarExame(id);
    }

    // ---------- Prescrições ----------

    @PostMapping("/prescricoes")
    @ResponseStatus(HttpStatus.CREATED)
    public Prescricao emitirPrescricao(@Valid @RequestBody Prescricao prescricao) {
        return service.emitirPrescricao(prescricao);
    }

    @GetMapping("/prescricoes")
    public List<Prescricao> listarPrescricoes(@RequestParam Long atendimentoId) {
        return service.listarPrescricoes(atendimentoId);
    }
}