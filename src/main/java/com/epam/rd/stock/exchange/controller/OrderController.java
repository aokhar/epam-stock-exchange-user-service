package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.OrderCreateDto;
import com.epam.rd.stock.exchange.dto.OrderViewDto;
import com.epam.rd.stock.exchange.facade.OrderFacade;
import com.epam.rd.stock.exchange.facade.ValuableFacade;
import com.epam.rd.stock.exchange.model.Order;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderFacade orderFacade;

    private final ValuableFacade valuableFacade;

    @GetMapping("/order")
    public String getNewOrderPage(Model model, @RequestParam String valuableId) {
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        if (valuableId != null) {
            orderCreateDto.setValuableId(valuableId);
        }
        model.addAttribute("newOrder", orderCreateDto);
        model.addAttribute("valuable", valuableFacade.findById(orderCreateDto.getValuableId()));
        return "newOrder";
    }

    @PostMapping("/order")
    public String submitOrder(@Valid @ModelAttribute(name = "newOrder") OrderCreateDto orderCreateDto,
                              BindingResult bindingResult, Authentication auth,
                              Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("newOrder", orderCreateDto);
            model.addAttribute("valuable", valuableFacade.findById(orderCreateDto.getValuableId()));
            return "newOrder";
        }
        orderCreateDto.setUserEmail(auth.getName());
        OrderViewDto orderViewDto = orderFacade.submit(orderCreateDto);
        if(orderViewDto.getStatus().equals(OrderStatus.SUCCESS))
        {
            redirectAttributes.addFlashAttribute("notification","Order was submitted!");
            return "redirect:/portfolio";
        }
        else{
            redirectAttributes.addFlashAttribute("error",orderViewDto.getFailDescription());
            return "redirect:/order?valuableId=" + orderCreateDto.getValuableId();
        }
    }

}
