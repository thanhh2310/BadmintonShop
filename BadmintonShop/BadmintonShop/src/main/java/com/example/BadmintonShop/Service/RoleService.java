package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.RoleRequest;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Enum.RoleName;
import com.example.BadmintonShop.Model.Role;
import com.example.BadmintonShop.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }

    public Role createRole(RoleRequest request) {
        // 1. Kiểm tra xem tên role đã tồn tại chưa
        if (roleRepository.findByRoleName(request.getRoleName()) != null) {
            throw new WebErrorConfig(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        // 2. Tạo và lưu role mới
        Role newRole = new Role();
        newRole.setRoleName(request.getRoleName());
        return roleRepository.save(newRole);
    }

    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.ROLE_NOT_FOUND));
    }

    public Role updateRole(Integer id, RoleRequest request) {
        // 1. Tìm role cũ
        Role existingRole = getRoleById(id);

        // 2. Kiểm tra xem tên mới đã bị role khác sử dụng chưa
        Role roleWithNewName = roleRepository.findByRoleName(request.getRoleName());
        if (roleWithNewName != null && !roleWithNewName.getId().equals(id)) {
            throw new WebErrorConfig(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        // 3. Cập nhật và lưu
        existingRole.setRoleName(request.getRoleName());
        return roleRepository.save(existingRole);
    }

    public void deleteRole(Integer id) {
        // 1. Tìm role
        Role roleToDelete = getRoleById(id);

        // 2. (Quan trọng) Không cho phép xóa các role cơ bản
        if (roleToDelete.getRoleName().equals(RoleName.ROLE_USER.name()) ||
                roleToDelete.getRoleName().equals(RoleName.ROLE_ADMIN.name())) {
            throw new WebErrorConfig(ErrorCode.CANNOT_DELETE_DEFAULT_ROLE);
        }

        try {
            // 3. Xóa role
            roleRepository.delete(roleToDelete);
        } catch (DataIntegrityViolationException e) {
            // 4. Bắt lỗi nếu Role này vẫn đang được User sử dụng (lỗi khóa ngoại)
            throw new WebErrorConfig(ErrorCode.ROLE_IN_USE);
        }
    }

}
