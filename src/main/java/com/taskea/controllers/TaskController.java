package com.taskea.controllers;

import com.taskea.model.Tarea;
import com.taskea.model.Turno;
import com.taskea.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TurnoRepository turnoRepository;
    @Autowired
    private TareaRepository tareaRepository;

    @GetMapping("/")
    public String home(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int pageCompleted
    ) {
        Pageable pageable = PageRequest.of(page, 5); // 5 tareas por página
        Pageable pageableCompleted = PageRequest.of(pageCompleted, 5);

        Page<Tarea> tareas = tareaRepository.findByTurnoIsNullAndCompletadaFalse(pageable);
        Page<Tarea> tareasCompletadas = tareaRepository.findByTurnoIsNullAndCompletadaTrue(pageableCompleted);

        model.addAttribute("tareas", tareas);
        model.addAttribute("tareasCompletadas", tareasCompletadas);
        model.addAttribute("nuevaTarea", new Tarea());
        return "index";
    }


    @PostMapping("/")
    public String addTask(@ModelAttribute("nuevaTarea") Tarea tarea) {
        tarea.setCompletada(false);
        tareaRepository.save(tarea);
        return "redirect:/";
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void reactivarPeriodicos() {
        List<Tarea> tareas = tareaRepository.findAll();
        for (Tarea tarea : tareas) {
            if (tarea.getPeriodicidad() != null && tarea.isCompletada()) {
                if (tarea.getFechaCompletada() != null &&
                        tarea.getFechaCompletada().plusDays(tarea.getPeriodicidad()).isBefore(LocalDateTime.now())) {
                    tarea.setCompletada(false);
                    tareaRepository.save(tarea);
                }
            }
        }
        // Repite lógica para compras
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void rotarTurnos() {
        Turno t1 = turnoRepository.findByNumero(1);
        Turno t2 = turnoRepository.findByNumero(2);
        String temp = t1.getEncargado();
        t1.setEncargado(t2.getEncargado());
        t2.setEncargado(temp);
        turnoRepository.save(t1);
        turnoRepository.save(t2);
    }

    @PostMapping("/completar/{id}")
    public String completarTarea(@PathVariable Long id) {
        tareaRepository.findById(id).ifPresent(t -> {
            if (t.isCompletada()) {
                t.setCompletada(false);
                t.setFechaCompletada(null);
            } else {
                t.setCompletada(true);
                t.setFechaCompletada(java.time.LocalDateTime.now());
            }
            tareaRepository.save(t);
        });
        return "redirect:/";
    }

    @PostMapping("/tarea/{id}/encargado")
    public String cambiarEncargado(@PathVariable Long id, @RequestParam String encargado) {
        tareaRepository.findById(id).ifPresent(t -> {
            t.setEncargado(encargado);
            tareaRepository.save(t);
        });
        return "redirect:/";
    }

    @GetMapping("/turnos")
    public String verTurnos(Model model) {
        Turno turno1 = turnoRepository.findByNumero(1);
        Turno turno2 = turnoRepository.findByNumero(2);
        model.addAttribute("turno1", turno1);
        model.addAttribute("turno2", turno2);
        model.addAttribute("nuevaTarea", new Tarea());
        return "turnos";
    }

    @PostMapping("/turnos/{numero}/tarea")
    public String agregarTarea(@PathVariable int numero, @ModelAttribute Tarea nuevaTarea) {
        Turno turno = turnoRepository.findByNumero(numero);
        nuevaTarea.setTurno(turno);
        tareaRepository.save(nuevaTarea);
        return "redirect:/turnos";
    }

    @PostMapping("/turnos/intercambiar")
    public String intercambiarEncargados() {
        Turno t1 = turnoRepository.findByNumero(1);
        Turno t2 = turnoRepository.findByNumero(2);
        String temp = t1.getEncargado();
        t1.setEncargado(t2.getEncargado());
        t2.setEncargado(temp);
        turnoRepository.save(t1);
        turnoRepository.save(t2);
        return "redirect:/turnos";
    }

    @PostMapping("/turnos/tarea/{id}/eliminar")
    public String eliminarTareaDeTurno(@PathVariable Long id) {
        tareaRepository.deleteById(id);
        return "redirect:/turnos";
    }
}