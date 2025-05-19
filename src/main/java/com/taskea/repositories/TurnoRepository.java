package com.taskea.repositories;

import com.taskea.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
    Turno findByNumero(int numero);
}