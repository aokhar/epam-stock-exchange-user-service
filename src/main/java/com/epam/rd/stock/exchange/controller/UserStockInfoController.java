package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.UserStockInfoUpdateDto;
import com.epam.rd.stock.exchange.facade.StockFacade;
import com.epam.rd.stock.exchange.facade.UserStockInfoFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserStockInfoController {

    private final StockFacade stockFacade;

    private final UserStockInfoFacade userStockInfoFacade;

    @GetMapping("/updateUserStockInfo")
    public String getUpdateUserStockInfoPage(Model model, @RequestParam String userStockInfoId) {
        UserStockInfoUpdateDto userStockInfoUpdateDto = userStockInfoFacade.findById(userStockInfoId);
        model.addAttribute("userStockInfo", userStockInfoUpdateDto);
        model.addAttribute("stock", stockFacade.findById(userStockInfoUpdateDto.getStockId()));
        return "updateUserStockInfo";
    }

    @PostMapping("/updateUserStockInfo")
    public String updateUserStockInfo(@Valid @ModelAttribute(name = "userStockInfo") UserStockInfoUpdateDto userStockInfoUpdateDto,
                                      BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            userStockInfoFacade.updateUserStockInfo(userStockInfoUpdateDto);
        }
        return "redirect:/portfolio";
    }
}
