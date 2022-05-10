package com.epam.rd.stock.exchange.service.unit;

import com.epam.rd.stock.exchange.datagenerator.EntityGenerator;
import com.epam.rd.stock.exchange.model.UserStockInfo;
import com.epam.rd.stock.exchange.repository.UserStockInfoRepository;
import com.epam.rd.stock.exchange.service.UserStockInfoService;
import com.epam.rd.stock.exchange.service.impl.UserStockInfoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserStockInfoTest {

    private UserStockInfoRepository userStockInfoRepository = mock(UserStockInfoRepository.class);

    private UserStockInfoService userStockInfoService = new UserStockInfoServiceImpl(userStockInfoRepository);

    private UserStockInfo userStockInfo;

    @BeforeEach
    public void init() {
        userStockInfo = EntityGenerator.generateUserStockInfo();
        userStockInfo.setUser(EntityGenerator.generateDomainUser());
        userStockInfo.setStock(EntityGenerator.generateDomainStock());
    }

    @Test
    public void shouldSaveUserStockInfo() {
        //Given
        when(userStockInfoRepository.findByUserIdAndStockId(userStockInfo.getUser().getId(), userStockInfo.getStock().getId())).thenReturn(Optional.of(userStockInfo));
        when(userStockInfoRepository.save(userStockInfo)).thenReturn(userStockInfo);

        //When
        UserStockInfo userStockInfoSaved = userStockInfoService.save(userStockInfo);

        //Then
        assertEquals(userStockInfo, userStockInfoSaved);
    }
}
