package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "Interface for employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation(value = "Employee Login")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation(value = "Employee Logout")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

//     add new employee
    @PostMapping
    @ApiOperation("Add new employee")
    public Result<Void> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("adding new employee: {}", employeeDTO);
        System.out.println("Thread id: " + Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return Result.success();
    }

//    pageQuery
    @ApiOperation("Employee pageQuery")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("employee pageQuery: {}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

//    change status
    @ApiOperation("Change status")
    @PostMapping("/status/{status}")
    public Result<Void> changeStatus(@PathVariable Integer status, Long id) {
        log.info("change status: {}, {}", status, id);
        employeeService.changeStatus(status, id);
        return Result.success();
    }

//    queryEmployee by Id
    @ApiOperation("Get employee by id")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("getById: {}", id);
        return Result.success(employeeService.getById(id));
    }

//    save changing
    @ApiOperation("Update employee info")
    @PutMapping
    public Result<Void> update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("update: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
