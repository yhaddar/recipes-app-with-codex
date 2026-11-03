package com.recipemanager.auth;

import java.util.UUID;

import com.recipemanager.dto.response.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping("/{userId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AccountStatusResponse> deactivate(@PathVariable UUID userId) {
        return ApiResponse.of(adminUserService.deactivate(userId), "Account deactivated");
    }

    @PostMapping("/{userId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<AccountStatusResponse> activate(@PathVariable UUID userId) {
        return ApiResponse.of(adminUserService.activate(userId), "Account activated");
    }
}
