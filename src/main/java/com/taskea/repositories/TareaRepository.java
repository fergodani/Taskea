package com.taskea.repositories;

import com.taskea.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    Page<Tarea> findByCompletadaFalse(Pageable pageable);
    Page<Tarea> findByCompletadaTrue(Pageable pageable);
    Page<Tarea> findByTurnoIsNull(Pageable pageable);
    Page<Tarea> findByTurnoIsNullAndCompletadaTrue(Pageable pageable);
    Page<Tarea> findByTurnoIsNullAndCompletadaFalse(Pageable pageable);
}