package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.AlertViewDto;
import com.epam.rd.stock.exchange.dto.ValuableViewDto;
import com.epam.rd.stock.exchange.facade.AlertFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AlertController {

    private final AlertFacade alertFacade;

    @Value("${pagination.amount}")
    private int pageSize;

    @GetMapping("/alerts")
    public String get(@RequestParam(name = "pageNumber", required = false) Integer pageNumber, Model model, Authentication auth){
        pageNumber = pageNumber == null ? 1 : pageNumber;
        String userEmail = auth.getName();
        Page<AlertViewDto> pageAlerts = alertFacade.getAll(userEmail, pageNumber, pageSize);
        List<AlertViewDto> alerts = pageAlerts.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", pageAlerts.getTotalPages());
        model.addAttribute("totalItems", pageAlerts.getTotalElements());
        model.addAttribute("notifications", alerts);
        return "alerts";
    }

    @PostMapping("/submit")
    public String delete(@RequestParam(name = "alertId", required = true)String alertId, Authentication auth,
                         RedirectAttributes redirectAttributes){
        String userEmail = auth.getName();
        alertFacade.delete(userEmail, alertId);
        redirectAttributes.addFlashAttribute("notification","Alert are deleted!");
        return "redirect:/alerts";
    }

    @PostMapping("/submitAll")
    public String deleteAll(Authentication auth, RedirectAttributes redirectAttributes){
        String userEmail = auth.getName();

        alertFacade.deleteAll(userEmail);
        redirectAttributes.addFlashAttribute("notification","All alerts are submited!");
        return "redirect:/alerts";
    }
}
