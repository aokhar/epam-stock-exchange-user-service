package com.epam.rd.stock.exchange.controller;

import com.epam.rd.stock.exchange.dto.CreateSubscriptionDto;
import com.epam.rd.stock.exchange.dto.SubscriptionViewDto;
import com.epam.rd.stock.exchange.dto.UpdateSubscriptionDto;
import com.epam.rd.stock.exchange.facade.SubscriptionFacade;
import com.epam.rd.stock.exchange.model.enums.ConditionType;
import com.epam.rd.stock.exchange.model.enums.SubscriptionType;
import com.epam.rd.stock.exchange.service.ValuableService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionFacade subscriptionFacade;

    private final ValuableService valuableService;

    @Value("${subscription.pagination.size}")
    private int pageSize;

    @GetMapping("/subscriptions")
    public String get(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                      @RequestParam(name = "type" , required = false) SubscriptionType subscriptionType,
                      Model model, Authentication auth) {
        pageNumber = pageNumber == null ? 1 : pageNumber;
        String email = auth.getName();
        Page<SubscriptionViewDto> pageSubscriptions;
        if (subscriptionType != null){
            pageSubscriptions = subscriptionFacade.get(email, subscriptionType, pageNumber, pageSize);
            model.addAttribute("type", subscriptionType);
        }
        else{
            pageSubscriptions = subscriptionFacade.get(email, pageNumber, pageSize);
        }
        List<SubscriptionViewDto> subscriptions = pageSubscriptions.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", pageSubscriptions.getTotalPages());
        model.addAttribute("totalItems", pageSubscriptions.getTotalElements());
        model.addAttribute("subscriptions", subscriptions);
        return "subscriptions";
    }

    @GetMapping("/newSubscription")
    public String newSubscription(@RequestParam(name = "valuableId", required = true) String valuableId,
                                  @RequestParam(name = "subscriptionType", required = false)SubscriptionType subscriptionType,
                                  @RequestParam(name = "conditionType", required = false) ConditionType conditionType,
                                  Model model){
        if(subscriptionType==null){
            subscriptionType = SubscriptionType.SELL;
        }
        if(conditionType == null){
            conditionType = ConditionType.STOP_LOSS_AND_TAKE_PROFIT;
        }
        CreateSubscriptionDto createSubscriptionDto = new CreateSubscriptionDto();
        createSubscriptionDto.setSubscriptionType(subscriptionType);
        createSubscriptionDto.setValuableId(valuableId);
        createSubscriptionDto.setConditionType(conditionType);
        model.addAttribute("newSubscription",createSubscriptionDto);
        model.addAttribute("conditionType", conditionType);
        model.addAttribute("subscriptionType",subscriptionType);
        model.addAttribute("valuableId", valuableId);
        model.addAttribute("valuable",valuableService.findById(valuableId));
        return "newSubscription";
    }

    @PostMapping("/newSubscription")
    public String newSubscription(@Valid @ModelAttribute("newSubscription") CreateSubscriptionDto createSubscriptionDto,
                                  Model model, Authentication auth,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "redirect:/subscriptions";
        }
        String email = auth.getName();
        SubscriptionViewDto subscriptionViewDto = subscriptionFacade.createSubscription(email, createSubscriptionDto);
        redirectAttributes.addFlashAttribute("notification",
                String.format("Subscription with condition %s was created!", subscriptionViewDto.getCondition()));
        return "redirect:/subscriptions";
    }

    @PostMapping("/removeSubscription")
    public String updateSubscription(@RequestParam(name = "subscriptionId", required = true)String subscriptionId,
                                     Authentication auth, RedirectAttributes redirectAttributes){
        String email = auth.getName();
        subscriptionFacade.deleteSubscription(email, subscriptionId);
        redirectAttributes.addFlashAttribute("notification", "Subscription were deleted!");
        return "redirect:/subscriptions";
    }
}
