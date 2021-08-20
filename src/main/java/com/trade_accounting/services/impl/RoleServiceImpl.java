package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RoleDto;
import com.trade_accounting.services.interfaces.RoleService;
import com.trade_accounting.services.interfaces.api.RoleApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleApi roleApi;

    private final String roleUrl;

    private List<RoleDto> roleDtoList = new ArrayList<>();

    private RoleDto roleDto = new RoleDto();

    private final CallExecuteService<RoleDto> dtoCallExecuteService;

    public RoleServiceImpl(@Value("${role_url}") String roleUrl, Retrofit retrofit, CallExecuteService<RoleDto> dtoCallExecuteService) {
        this.roleUrl = roleUrl;
        roleApi = retrofit.create(RoleApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<RoleDto> getAll() {
        Call<List<RoleDto>> roleGetAllCall = roleApi.getAll(roleUrl);
        return dtoCallExecuteService.callExecuteBodyList(roleGetAllCall, RoleDto.class);
    }

    @Override
    public RoleDto getById(Long id) {
        Call<RoleDto> roleGetCall = roleApi.getById(roleUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(roleGetCall,roleDto, RoleDto.class, id);
    }

    @Override
    public void create(RoleDto roleDto) {
        Call<Void> roleCall = roleApi.create(roleUrl, roleDto);
        dtoCallExecuteService.callExecuteBodyCreate(roleCall, RoleDto.class);
    }

    @Override
    public void update(RoleDto roleDto) {
        Call<Void> roleUpdateCall = roleApi.update(roleUrl, roleDto);
        dtoCallExecuteService.callExecuteBodyUpdate(roleUpdateCall, RoleDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> roleDeleteCall = roleApi.deleteById(roleUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(roleDeleteCall, RoleDto.class, id);
    }
}