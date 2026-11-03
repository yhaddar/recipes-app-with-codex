package com.recipemanager.auth;

import com.recipemanager.dto.response.ApiResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class PasswordRecoveryController {

    private final PasswordRecoveryService recoveryService;

    public PasswordRecoveryController(PasswordRecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> requestReset(@RequestParam @NotBlank @Email String email) {
        recoveryService.requestReset(email);
        return ApiResponse.of(null, "If the account exists, a reset email will be sent");
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @RequestParam @NotBlank String token,
            @RequestParam @NotBlank @Size(min = 8, max = 72) String newPassword) {
        recoveryService.resetPassword(token, newPassword);
        return ApiResponse.of(null, "Password reset successful");
    }
}
