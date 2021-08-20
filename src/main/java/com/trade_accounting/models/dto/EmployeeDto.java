package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;

    private String lastName;

    private String firstName;

    private String middleName;

    private String sortNumber;

    private String phone;

    @Pattern(regexp = "([0-9]+){12}")
    private String inn;

    private String description;

    @Email(message = "Please enter a valid e-mail address")
    private String email;

    private String password;

    private DepartmentDto departmentDto;

    private PositionDto positionDto;

    private Set<RoleDto> roleDto;

    private ImageDto imageDto;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
