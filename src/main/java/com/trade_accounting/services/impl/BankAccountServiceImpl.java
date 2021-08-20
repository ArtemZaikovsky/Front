package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.services.interfaces.BankAccountService;
import com.trade_accounting.services.interfaces.api.BankAccountApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountApi bankAccountApi;
    private final String bankAccountUrl;
    private final CallExecuteService<BankAccountDto> dto;

    public BankAccountServiceImpl(@Value("${bank_account_url}") String bankAccountUrl, Retrofit retrofit, CallExecuteService<BankAccountDto> dto) {

        this.bankAccountUrl = bankAccountUrl;
        bankAccountApi = retrofit.create(BankAccountApi.class);
        this.dto = dto;
    }

    @Override
    public List<BankAccountDto> getAll() {
        Call<List<BankAccountDto>> bankAccountDtoListCall = bankAccountApi.getAll(bankAccountUrl);
        return dto.callExecuteBodyList(bankAccountDtoListCall, BankAccountDto.class);
    }

    @Override
    public BankAccountDto getById(Long id) {
        BankAccountDto bankAccountDto = new BankAccountDto();
        Call<BankAccountDto> bankAccountDtoCall = bankAccountApi.getById(bankAccountUrl, id);
        return dto.callExecuteBodyById(bankAccountDtoCall, bankAccountDto, BankAccountDto.class, id);
    }


    @Override
    public BankAccountDto create(BankAccountDto bankAccountDto) {
        Call<BankAccountDto> bankAccountDtoCall = bankAccountApi.create(bankAccountUrl, bankAccountDto);
        BankAccountDto result=null;
        try {
            result = bankAccountDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание экземпляра BankAccountDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра BankAccountDto - {}", e);
        }
        return result;
        //  return dto.callExecuteBodyCreate(bankAccountDtoCall, BankAccountDto.class);
    }

    @Override
    public void update(BankAccountDto bankAccountDto) {
        Call<Void> bankAccountDtoCall = bankAccountApi.update(bankAccountUrl, bankAccountDto);
        dto.callExecuteBodyUpdate(bankAccountDtoCall, BankAccountDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> bankAccountDtoCall = bankAccountApi.deleteById(bankAccountUrl, id);
        dto.callExecuteBodyDelete(bankAccountDtoCall, BankAccountDto.class, id);
    }
}
