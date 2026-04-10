package com.demo.butaca.controller;

import com.demo.butaca.dto.ComprarEntradaRequest;
import com.demo.butaca.dto.CrearEventoRequest;
import com.demo.butaca.dto.ValidarEntradaRequest;
import com.demo.butaca.model.Entrada;
import com.demo.butaca.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {
    private final TicketService ticketService;

    public WebController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/eventos";
    }

    @GetMapping("/eventos")
    public String verEventos(Model model) {
        model.addAttribute("eventos", ticketService.listarEventos());
        model.addAttribute("crearEventoRequest", new CrearEventoRequest());
        model.addAttribute("comprarEntradaRequest", new ComprarEntradaRequest());
        model.addAttribute("validarEntradaRequest", new ValidarEntradaRequest());
        return "eventos";
    }

    @PostMapping("/eventos")
    public String crearEvento(@ModelAttribute CrearEventoRequest request, RedirectAttributes redirectAttributes) {
        try {
            ticketService.crearEvento(request);
            redirectAttributes.addFlashAttribute("mensaje", "Evento creado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/eventos";
    }

    @PostMapping("/entradas/comprar")
    public String comprarEntrada(@ModelAttribute ComprarEntradaRequest request, RedirectAttributes redirectAttributes) {
        try {
            Entrada entrada = ticketService.comprarEntrada(request.getEventoId());
            redirectAttributes.addFlashAttribute("mensaje",
                    "Compra exitosa. Codigo unico: " + entrada.getCodigoUnico());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/eventos";
    }

    @PostMapping("/entradas/validar")
    public String validarEntrada(@ModelAttribute ValidarEntradaRequest request, RedirectAttributes redirectAttributes) {
        String resultado = ticketService.validarEntrada(request.getCodigoUnico());
        if (resultado.startsWith("Entrada valida")) {
            redirectAttributes.addFlashAttribute("mensaje", resultado);
        } else {
            redirectAttributes.addFlashAttribute("error", resultado);
        }
        return "redirect:/eventos";
    }
}
