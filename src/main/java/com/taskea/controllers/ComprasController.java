package com.taskea.controllers;

import com.taskea.model.Compra;
import com.taskea.repositories.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/compras")
public class ComprasController {

    @Autowired
    private CompraRepository compraRepository;

    @GetMapping
    public String listarCompras(Model model) {
        model.addAttribute("compras", compraRepository.findAll());
        List<String> categorias = List.of("Aceite, especias y salsas", "Bebida", "Piqui"
                , "Arroz, legumbres y pasta", "Café e infusiones", "Carne", "Cereales", "Charcutería y quesos"
                , "Congelados", "Higiene", "Fruta y verdura", "Limpieza", "Mascotas", "Lácteos", "Pescado", "Otros");
        List<String> listaOrdenada = new ArrayList<>(categorias);
        Collections.sort(listaOrdenada);
        model.addAttribute("categorias", listaOrdenada);
        return "compras";
    }

    @PostMapping("/agregar")
    public String agregarCompra(@RequestParam String producto,
                                @RequestParam(required = false) Integer cantidad,
                                @RequestParam(required = false) String unidad,
                                @RequestParam(required = false) String categoria,
                                @RequestParam(required = false) String nota) {
        Compra compra = new Compra();
        compra.setProducto(producto);
        compra.setCantidad(cantidad != null ? cantidad : 1);
        compra.setUnidad(unidad);
        compra.setCategoria(categoria);
        compra.setNota(nota);
        compra.setPrioridad(0);
        compra.setComprado(false);
        compra.setFechaAgregado(LocalDateTime.now());
        compraRepository.save(compra);
        return "redirect:/compras";
    }

    @PostMapping("/toggle/{id}")
    public String toggleComprado(@PathVariable Long id) {
        Optional<Compra> compraOpt = compraRepository.findById(id);
        if (compraOpt.isPresent()) {
            Compra compra = compraOpt.get();
            compra.setComprado(!Boolean.TRUE.equals(compra.getComprado()));
            compraRepository.save(compra);
        }
        return "redirect:/compras";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCompra(@PathVariable Long id) {
        compraRepository.deleteById(id);
        return "redirect:/compras";
    }

    @PostMapping("/incrementar/{id}")
    public String incrementarCantidad(@PathVariable Long id) {
        compraRepository.findById(id).ifPresent(compra -> {
            compra.setCantidad(compra.getCantidad() + 1);
            compraRepository.save(compra);
        });
        return "redirect:/compras";
    }

    @PostMapping("/decrementar/{id}")
    public String decrementarCantidad(@PathVariable Long id) {
        compraRepository.findById(id).ifPresent(compra -> {
            if (compra.getCantidad() > 1) {
                compra.setCantidad(compra.getCantidad() - 1);
                compraRepository.save(compra);
            }
        });
        return "redirect:/compras";
    }
}