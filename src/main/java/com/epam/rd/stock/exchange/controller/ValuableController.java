package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.ValuableViewDto;
import com.epam.rd.stock.exchange.facade.StockFacade;
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

    private final StockFacade stockFacade;

    @Value("${pagination.amount}")
    private int pageSize;

    @GetMapping("/")
    public String getMainPage(@RequestParam(name = "pageNumber", required = false) Integer pageNumber, @RequestParam(name = "symbol", required = false) String symbol, Model model) {
        symbol = symbol == null ? "" : symbol;
        pageNumber = pageNumber == null ? 1 : pageNumber;
        Page<ValuableViewDto> pageValuables = stockFacade.findStocksBySymbol(symbol, pageNumber, pageSize);
        List<ValuableViewDto> valuables = pageValuables.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", pageValuables.getTotalPages());
        model.addAttribute("totalItems", pageValuables.getTotalElements());
        model.addAttribute("valuables", valuables);
        model.addAttribute("symbol", symbol);
        return "index";
    }
}
