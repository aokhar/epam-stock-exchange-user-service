package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.OrderViewDto;
import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.dto.UserViewDto;
import com.epam.rd.stock.exchange.facade.OrderFacade;
import com.epam.rd.stock.exchange.facade.UserFacade;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;
    private final OrderFacade orderFacade;

    @Value("${table.pagination.amount}")
    private int tableSize;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }


    @PostMapping("/login")
    public String login() {
        return "redirect:/portfolio";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        UserCreateDto userCreateDto = new UserCreateDto();
        model.addAttribute("user", userCreateDto);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") UserCreateDto userCreateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userFacade.registration(userCreateDTO);
        return "redirect:/registration?success";
    }

    @RequestMapping(path = "/loginError", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginError(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", request.getAttribute("error"));
        return "redirect:/login";
    }

    @GetMapping("/portfolio")
    public String getPortfolioPage(Model model, Authentication auth) {
        UserViewDto user = userFacade.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "portfolio";
    }

    @GetMapping("/orders")
    public String getOrdersByStatusPaginated(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                             @RequestParam(name = "status", required = false) OrderStatus status,
                                             Model model, Authentication auth) {
        pageNumber = pageNumber == null ? 1 : pageNumber;
        UserViewDto user = userFacade.findByEmail(auth.getName());
        Page<OrderViewDto> ordersPage = orderFacade.findByUserIdAndStatus(user.getId(), status, pageNumber, tableSize);
        List<OrderViewDto> ordersStatus = ordersPage.getContent();
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("totalItems", ordersPage.getTotalElements());
        model.addAttribute("status", status);
        model.addAttribute("orders", ordersStatus);
        return "orders";
    }

}
