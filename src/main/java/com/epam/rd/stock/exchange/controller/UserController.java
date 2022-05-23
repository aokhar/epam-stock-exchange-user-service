package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.ChangeWalletBalanceDto;
import com.epam.rd.stock.exchange.dto.OrderViewDto;
import com.epam.rd.stock.exchange.dto.UserCreateDto;
import com.epam.rd.stock.exchange.dto.UserViewDto;
import com.epam.rd.stock.exchange.facade.OrderFacade;
import com.epam.rd.stock.exchange.facade.UserFacade;
import com.epam.rd.stock.exchange.facade.WalletFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;
    private final OrderFacade orderFacade;

    private final WalletFacade walletFacade;

    @Value("${pagination.amount}")
    private int pageSize;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        return "redirect:/profile";
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

    @GetMapping("/portfolio")
    public String getPortfolioPage(Model model, Authentication auth) {
        UserViewDto user = userFacade.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "portfolio";
    }


    @GetMapping("/wallet")
    public String getWalletPage(Model model, Authentication auth) {
        UserViewDto user = userFacade.findByEmail(auth.getName());
        ChangeWalletBalanceDto wallet = walletFacade.findByUserId(user.getId());
        model.addAttribute("walletUpdate", wallet);
        return "paymentPage";
    }

    @PostMapping("/updateBalance")
    public String getWalletPage(@Valid @ModelAttribute("walletUpdate") ChangeWalletBalanceDto changeWalletBalanceDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "paymentPage";
        } else {
            changeWalletBalanceDto.setBalance((BigDecimal) request.getSession().getAttribute("balance"));
            walletFacade.changeBalance(changeWalletBalanceDto);
            return "redirect:/wallet";
        }
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, Authentication auth, HttpServletRequest request) {
        UserViewDto user = userFacade.findByEmail(auth.getName());
        model.addAttribute("user", user);
        request.getSession().setAttribute(HttpHeaders.REFERER,
                request.getRequestURI().replaceAll("\\?.*", ""));
        return "profile";
    }

    @RequestMapping(path = "/loginError", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginError(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", request.getAttribute("error"));
        return "redirect:/login";
    }

    @GetMapping("/orders")
    public String getOrdersByStatusPaginated(@RequestParam(name = "pageNumber", required = false) Integer pageNumber, Model model, Authentication auth) {
        pageNumber = pageNumber == null ? 1 : pageNumber;
        UserViewDto user = userFacade.findByEmail(auth.getName());
        Page<OrderViewDto> ordersPage = orderFacade.findByUserIdAndStatus(user.getId(), pageNumber, pageSize);
        List<OrderViewDto> ordersStatus = ordersPage.getContent();
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("totalItems", ordersPage.getTotalElements());
        model.addAttribute("orders", ordersStatus);
        return "orders";
    }

}
