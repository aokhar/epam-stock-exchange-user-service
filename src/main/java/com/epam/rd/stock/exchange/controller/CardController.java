package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.ChangeBalanceDto;
import com.epam.rd.stock.exchange.dto.NewCardDto;
import com.epam.rd.stock.exchange.dto.UpdateCardDto;
import com.epam.rd.stock.exchange.dto.UserCardViewDto;
import com.epam.rd.stock.exchange.dto.enums.ChangeBalanceType;
import com.epam.rd.stock.exchange.exception.CardExistException;
import com.epam.rd.stock.exchange.facade.CardFacade;
import com.epam.rd.stock.exchange.facade.UserFacade;
import liquibase.pro.packaged.C;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CardController {

    @Value("${pagination.amount}")
    private int pageSize;

    private final CardFacade cardFacade;
    private final UserFacade userFacade;

    @GetMapping("/cards")
    public String getAll(@RequestParam(name = "pageNumber", required = false) Integer pageNumber, Model model, Authentication auth) {
        pageNumber = pageNumber == null ? 1 : pageNumber;
        Page<UserCardViewDto> pageCard = cardFacade.findCardsByUserEmail(auth.getName(), pageNumber, pageSize);
        List<UserCardViewDto> cards = pageCard.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", pageCard.getTotalPages());
        model.addAttribute("totalItems", pageCard.getTotalElements());
        model.addAttribute("cards", cards);
        return "cards";
    }

    @GetMapping("/newCard")
    public String createNew(Model model) {
        NewCardDto card = new NewCardDto();

        model.addAttribute("newCard", card);
        return "newCard";
    }

    @PostMapping("/newCard")
    public String add(@Valid @ModelAttribute(name = "newCard") NewCardDto newCard,
                      BindingResult bindingResult, Authentication auth, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Card is not valid!");
            return "redirect:/newCard";
        }

        String email = auth.getName();
        cardFacade.addCard(email, newCard);
        redirectAttributes.addFlashAttribute("notification", String.format("Card %s are added", newCard.getCard()));
        return "redirect:/cards";
    }

    @GetMapping("/updateCard")
    public String updateCard(@RequestParam(name = "cardId", required = true) String cardId, Model model, Authentication auth) {
        String email = auth.getName();
        UserCardViewDto userCardViewDto = cardFacade.getCardById(email, cardId);

        UpdateCardDto newCardDto = toUpdateCardDto(userCardViewDto, cardId);

        model.addAttribute("cardUpdate", newCardDto);
        return "card";
    }

    @PostMapping("/updateCard")
    public String update(@Valid @ModelAttribute(name = "cardUpdate") UpdateCardDto updateCardDto ,
                         BindingResult bindingResult, Authentication auth, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            return "redirect:/updateCard?cardId=" + updateCardDto.getCardId();
        }

        String email = auth.getName();

        cardFacade.updateCard(email, updateCardDto);

        redirectAttributes.addFlashAttribute("notification", "Card are updated");
        return "redirect:/cards";
    }

    @PostMapping("/deleteCard")
    public String delete(@RequestParam(required = true) String cardId, Authentication auth,
                         RedirectAttributes redirectAttributes) {
        String email = auth.getName();
        Long card = cardFacade.removeCard(email, cardId);
        redirectAttributes.addFlashAttribute("notification",String.format("Card %s was deleted!", card));
        return "redirect:/cards";
    }

    @GetMapping("/updateBalance")
    public String updateBalance(@RequestParam(name = "cardId", required = false) String cardId, Model model, Authentication auth) {


        ChangeBalanceDto changeBalanceDto;
        if(cardId != null){
            String email = auth.getName();
            UserCardViewDto userCardViewDto = cardFacade.getCardById(email, cardId);
            changeBalanceDto = toChangeBalanceDto(userCardViewDto);
        }
        else{
            changeBalanceDto = new ChangeBalanceDto();
        }

        model.addAttribute("balanceUpdate", changeBalanceDto);
        return "/paymentPage";
    }

    @PostMapping("/updateBalance")
    public String updateBalance(@Valid @ModelAttribute("balanceUpdate") ChangeBalanceDto changeBalanceDto,
                                BindingResult bindingResult, Authentication auth, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            return "/updateBalance";
        }

        String email = auth.getName();


        BigDecimal update = userFacade.updateBalance(email, changeBalanceDto);
        if(changeBalanceDto.isSaveCard()){
            try{
                cardFacade.addCard(email, toNewCardDto(changeBalanceDto));
            }
            catch (CardExistException e){
                log.info("User already have this card");
            }
        }
        redirectAttributes.addFlashAttribute("notification",String.format("Balance were updated on value %s", update));
        return "redirect:/cards";
    }

    private ChangeBalanceDto toChangeBalanceDto(UserCardViewDto userCardViewDto){
        ChangeBalanceDto changeBalanceDto = new ChangeBalanceDto();
        changeBalanceDto.setCard(userCardViewDto.getCard());
        changeBalanceDto.setCardHolder(userCardViewDto.getCardHolder());
        changeBalanceDto.setCvc(userCardViewDto.getCvc());
        changeBalanceDto.setExpMonth(userCardViewDto.getExpMonth());
        changeBalanceDto.setExpYear(userCardViewDto.getExpYear());
        return changeBalanceDto;
    }

    private NewCardDto toNewCardDto(ChangeBalanceDto changeBalanceDto){
        NewCardDto newCardDto = new NewCardDto();
        newCardDto.setCard(changeBalanceDto.getCard());
        newCardDto.setCardHolder(changeBalanceDto.getCardHolder());
        newCardDto.setCvc(changeBalanceDto.getCvc());
        newCardDto.setExpMonth(changeBalanceDto.getExpMonth());
        newCardDto.setExpYear(changeBalanceDto.getExpYear());
        return newCardDto;
    }

    private UpdateCardDto toUpdateCardDto(UserCardViewDto userCardViewDto, String cardId){
        return UpdateCardDto.builder()
                .cardId(cardId)
                .card(userCardViewDto.getCard())
                .cardHolder(userCardViewDto.getCardHolder())
                .cvc(userCardViewDto.getCvc())
                .expMonth(userCardViewDto.getExpMonth())
                .expYear(userCardViewDto.getExpYear())
                .build();
    }

}
