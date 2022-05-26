package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.ValuableHistoryViewDto;
import com.epam.rd.stock.exchange.dto.ValuableViewDto;
import com.epam.rd.stock.exchange.facade.ValuableFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ValuableController {

    private final ValuableFacade valuableFacade;

    @Value("${pagination.amount}")
    private int pageSize;

    @Value("${table.pagination.amount}")
    private int tableSize;

    @GetMapping("/")
    public String getMainPage(@RequestParam(name = "pageNumber", required = false) Integer pageNumber, @RequestParam(name = "symbol", required = false) String symbol, Model model) {
        symbol = symbol == null ? "" : symbol;
        pageNumber = pageNumber == null ? 1 : pageNumber;
        Page<ValuableViewDto> pageValuables = valuableFacade.findStocksBySymbol(symbol, pageNumber, pageSize);
        List<ValuableViewDto> valuables = pageValuables.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", pageValuables.getTotalPages());
        model.addAttribute("totalItems", pageValuables.getTotalElements());
        model.addAttribute("valuables", valuables);
        model.addAttribute("symbol", symbol);
        return "index";
    }

    @GetMapping("/valuableHistory")
    public String getHistory(@RequestParam(name = "pageNumber", required = false) Integer pageNumber, @RequestParam(name = "valuableId", required = true) String valuableId, Model model) {
        pageNumber = pageNumber == null ? 1 : pageNumber;
        ValuableViewDto valuableViewDto = valuableFacade.findById(valuableId);
        Page<ValuableHistoryViewDto> valuableHistoriesPage = valuableFacade.findHistoryById(valuableId, pageNumber, tableSize);
        List<ValuableHistoryViewDto> valuableHistories = valuableHistoriesPage.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("valuable",valuableViewDto);
        model.addAttribute("totalPages", valuableHistoriesPage.getTotalPages());
        model.addAttribute("totalItems", valuableHistoriesPage.getTotalElements());
        model.addAttribute("valuableHistories", valuableHistories);
        model.addAttribute("valuableId", valuableId);
        return "valuableHistory";
    }
}
