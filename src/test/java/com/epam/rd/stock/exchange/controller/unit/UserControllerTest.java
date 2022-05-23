package com.epam.rd.stock.exchange.controller.unit;

import com.epam.rd.stock.exchange.controller.UserController;
import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.dto.ChangeWalletBalanceDto;
import com.epam.rd.stock.exchange.facade.OrderFacade;
import com.epam.rd.stock.exchange.facade.UserFacade;
import com.epam.rd.stock.exchange.facade.WalletFacade;
import com.epam.rd.stock.exchange.handler.GlobalExceptionHandler;
import com.epam.rd.stock.exchange.mapper.OrderMapper;
import com.epam.rd.stock.exchange.mapper.UserMapper;
import com.epam.rd.stock.exchange.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UserControllerTest {

    private MockMvc mockMvc;

    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private UserFacade userFacade = mock(UserFacade.class);

    private OrderFacade orderFacade = mock(OrderFacade.class);

    private WalletFacade walletFacade = mock(WalletFacade.class);

    private Authentication auth = mock(Authentication.class);

    private OrderMapper orderMapper;
    private UserMapper userMapper;
    private WalletMapper walletMapper;
    private User user;

    @BeforeEach
    public void init() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userFacade, orderFacade, walletFacade))
                .setViewResolvers(viewResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        walletMapper = new WalletMapper();
        userMapper = new UserMapper(passwordEncoder);
        orderMapper = new OrderMapper();
        user = EntityGenerator.generateDomainUser();
    }

    @Test
    public void shouldReturnLoginPage() throws Exception {
        //GIVEN

        //WHEN + THEN
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void shouldRedirectToLoginPage() throws Exception {
        //GIVEN

        //WHEN + THEN
        mockMvc.perform(post("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile"));
    }

    @Test
    public void shouldReturnRegistrationPage() throws Exception {
        //GIVEN

        //WHEN + THEN
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void shouldReturnPortfolioPage() throws Exception {
        //GIVEN
        when(auth.getName()).thenReturn(user.getEmail());
        when(userFacade.findByEmail(user.getEmail())).thenReturn(userMapper.toUserViewDto(user));

        //WHEN + THEN
        mockMvc.perform(get("/portfolio").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("portfolio"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void shouldReturnWalletPage() throws Exception {
        //GIVEN
        Wallet wallet = EntityGenerator.generateDomainWallet();
        when(auth.getName()).thenReturn(user.getEmail());
        when(userFacade.findByEmail(user.getEmail())).thenReturn(userMapper.toUserViewDto(user));
        when(walletFacade.findByUserId(user.getId())).thenReturn(walletMapper.toViewDto(wallet));

        //WHEN + THEN
        mockMvc.perform(get("/wallet").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentPage"))
                .andExpect(model().attributeExists("walletUpdate"));
    }

    @Test
    public void shouldCallUpdateBalancePageWithWrongModelAndGetError() throws Exception {
        //GIVEN
        Wallet wallet = new Wallet();
        wallet.setId("1234");
        wallet.setBalance(BigDecimal.valueOf(8000));
        wallet.setUserId("123");
        wallet.setCvc(1233456);
        wallet.setCardHolder("Card Holder test");
        wallet.setExpYear(12345);
        wallet.setExpMonth(123456);
        wallet.setCard(1111222233334444L);

        //WHEN + THEN
        mockMvc.perform(post("/updateBalance").flashAttr("walletUpdate", walletMapper.toViewDto(wallet)).sessionAttr("balance", BigDecimal.valueOf(100)))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentPage"));
    }

    @Test
    public void shouldCallUpdateBalancePage() throws Exception {
        //GIVEN
        Wallet wallet = new Wallet();
        wallet.setId("1234");
        wallet.setBalance(BigDecimal.valueOf(8000));
        wallet.setUserId("123");
        wallet.setCvc(123);
        wallet.setCardHolder("Card Holder test");
        wallet.setExpYear(1234);
        wallet.setExpMonth(12);
        wallet.setCard(11112222333344L);

        ChangeWalletBalanceDto changeWalletBalanceDto = walletMapper.toViewDto(wallet);
        changeWalletBalanceDto.setSum(BigDecimal.valueOf(100));

        //WHEN + THEN
        mockMvc.perform(post("/updateBalance").flashAttr("walletUpdate", changeWalletBalanceDto).sessionAttr("balance", BigDecimal.valueOf(100)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/wallet"));
    }

    @Test
    public void shouldReturnProfilePage() throws Exception {
        //GIVEN
        when(auth.getName()).thenReturn(user.getEmail());
        when(userFacade.findByEmail(user.getEmail())).thenReturn(userMapper.toUserViewDto(user));

        //WHEN + THEN
        mockMvc.perform(get("/profile").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"));
    }
}
