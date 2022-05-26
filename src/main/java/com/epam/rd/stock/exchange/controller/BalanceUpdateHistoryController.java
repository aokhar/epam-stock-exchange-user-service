package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.BalanceUpdateHistoryViewDto;
import com.epam.rd.stock.exchange.dto.UserCardViewDto;
import com.epam.rd.stock.exchange.facade.BalanceUpdateHistoryFacade;
import com.epam.rd.stock.exchange.model.enums.BalanceUpdateType;
import com.epam.rd.stock.exchange.service.BalanceUpdateHistoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BalanceUpdateHistoryController {

    private final BalanceUpdateHistoryFacade balanceUpdateHistoryFacade;

    @Value("${pagination.amount}")
    private int pageSize;

    @GetMapping("/balanceUpdates")
    public String get(@RequestParam(name = "type", required = false) BalanceUpdateType balanceUpdateType, @RequestParam(name = "pageNumber", required = false) Integer pageNumber, Model model, Authentication auth){
        pageNumber = pageNumber == null ? 1 : pageNumber;
        String userEmail = auth.getName();
        Page<BalanceUpdateHistoryViewDto> pageHistories = balanceUpdateHistoryFacade.get(userEmail, pageNumber, pageSize, balanceUpdateType);
        List<BalanceUpdateHistoryViewDto> balanceHistoryUpdates = pageHistories.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", pageHistories.getTotalPages());
        model.addAttribute("totalItems", pageHistories.getTotalElements());
        model.addAttribute("balanceHistoryUpdates", balanceHistoryUpdates);
        return "balanceUpdateHistory";
    }
}
