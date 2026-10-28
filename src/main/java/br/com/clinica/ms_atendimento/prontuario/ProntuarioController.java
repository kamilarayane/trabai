package br.com.clinica.ms_atendimento.prontuario;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    private final ProntuarioService service;

    public ProntuarioController(ProntuarioService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prontuario criar(@Valid @RequestBody Prontuario prontuario) {
        return service.criar(prontuario);
    }

    @GetMapping("/{id}")
    public Prontuario buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public Prontuario buscarPorPaciente(@PathVariable Long pacienteId) {
        return service.buscarPorPaciente(pacienteId);
    }

    @PutMapping("/{id}")
    public Prontuario atualizar(@PathVariable Long id, @RequestBody Prontuario dados) {
        return service.atualizar(id, dados);
    }

    @PostMapping("/{id}/evolucoes")
    @ResponseStatus(HttpStatus.CREATED)
    public EvolucaoClinica registrarEvolucao(@PathVariable Long id,
                                             @Valid @RequestBody EvolucaoClinica evolucao) {
        return service.registrarEvolucao(id, evolucao);
    }

    @GetMapping("/{id}/evolucoes")
    public List<EvolucaoClinica> listarEvolucoes(@PathVariable Long id) {
        return service.listarEvolucoes(id);
    }
}