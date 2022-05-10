package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.ChangeWalletBalanceDto;
import com.epam.rd.stock.exchange.dto.UserViewAdminDto;
import com.epam.rd.stock.exchange.facade.UserFacade;
import com.epam.rd.stock.exchange.facade.WalletFacade;
import com.epam.rd.stock.exchange.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserFacade userFacade;

    private final WalletFacade walletFacade;

    @Value("${pagination.amount}")
    private int pageSize;

    @GetMapping("/users")
    public String getUsersPage(@RequestParam(name = "pageNumber", required = false) Integer pageNumber, @RequestParam(name = "email", required = false) String email, Model model) {
        email = email == null ? "" : email;
        pageNumber = pageNumber == null ? 1 : pageNumber;
        Page<UserViewAdminDto> pageUsers = userFacade.findAllUsersByEmail(email, pageNumber, pageSize);
        List<UserViewAdminDto> users = pageUsers.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", pageUsers.getTotalPages());
        model.addAttribute("totalItems", pageUsers.getTotalElements());
        model.addAttribute("users", users);
        model.addAttribute("email", email);
        return "users";
    }

    @GetMapping("/block")
    public String blockUser(@RequestParam(name = "userId", required = true) String userId, @RequestParam(name = "operation", required = true) boolean operation, Model model) {
        userFacade.updateBlockingStatus(userFacade.findById(userId).getEmail(), operation);
        return "redirect:/admin/users";
    }

    @GetMapping("/changeRole")
    public String changeRole(@RequestParam(name = "userId", required = true) String userId, @RequestParam(name = "role", required = true) UserRole role, Model model) {
        userFacade.updateRole(userFacade.findById(userId).getEmail(), role);
        return "redirect:/admin/users";
    }

    @GetMapping("/changeUserBalance")
    public String changeUserBalance(@RequestParam(name = "userId", required = true) String userId, @RequestParam(name = "balance", required = true) BigDecimal balance, Model model) {
        ChangeWalletBalanceDto changeWalletBalanceDto = walletFacade.findByUserId(userId);
        changeWalletBalanceDto.setSum(balance.subtract(changeWalletBalanceDto.getBalance()));
        walletFacade.changeBalance(changeWalletBalanceDto);
        return "redirect:/admin/users";
    }
}
