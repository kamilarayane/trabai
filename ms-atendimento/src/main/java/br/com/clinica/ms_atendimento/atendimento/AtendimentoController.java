package br.com.clinica.ms_atendimento.atendimento;

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
@RequestMapping("/atendimentos")
public class AtendimentoController {

    private final AtendimentoService service;

    public AtendimentoController(AtendimentoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Atendimento abrir(@Valid @RequestBody Atendimento atendimento) {
        return service.abrir(atendimento);
    }

    @GetMapping("/{id}")
    public Atendimento buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping
    public List<Atendimento> listar(@RequestParam(required = false) Long pacienteId) {
        if (pacienteId != null) {
            return service.listarPorPaciente(pacienteId);
        }
        return service.listarTodos();
    }

    @PatchMapping("/{id}/finalizar")
    public Atendimento finalizar(@PathVariable Long id) {
        return service.finalizar(id);
    }
}