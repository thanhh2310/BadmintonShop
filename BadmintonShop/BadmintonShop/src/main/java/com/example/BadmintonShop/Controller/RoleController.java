package com.example.BadmintonShop.Controller;

import com.example.BadmintonShop.DTO.Request.RoleRequest;
import com.example.BadmintonShop.DTO.Response.ApiResponse;
import com.example.BadmintonShop.Model.Role;
import com.example.BadmintonShop.Service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles") // Đường dẫn (endpoint) cơ sở là /roles
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Role> createRole(@RequestBody RoleRequest request) {
        Role newRole = roleService.createRole(request);
        return ApiResponse.<Role>builder()
                .code(200)
                .message("Role created successfully")
                .data(newRole)
                .build();
    }

    @GetMapping
//    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRole();
        return ApiResponse.<List<Role>>builder()
                .code(200)
                .message("Roles retrieved successfully")
                .data(roles)
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Role> getRoleById(@PathVariable Integer id) {
        Role role = roleService.getRoleById(id);
        return ApiResponse.<Role>builder()
                .code(200)
                .message("Role retrieved successfully")
                .data(role)
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Role> updateRole(@PathVariable Integer id, @RequestBody RoleRequest request) {
        Role updatedRole = roleService.updateRole(id, request);
        return ApiResponse.<Role>builder()
                .code(200)
                .message("Role updated successfully")
                .data(updatedRole)
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Role deleted successfully")
                .build();
    }
}
