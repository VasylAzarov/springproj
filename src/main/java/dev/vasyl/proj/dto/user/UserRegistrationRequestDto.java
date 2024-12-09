package dev.vasyl.proj.dto.user;

import dev.vasyl.proj.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@ValidPassword(passwordField = "password", confirmPasswordField = "confirmedPassword")
public class UserRegistrationRequestDto {
    @Schema(description = "User email",
            example = "user@email.com")
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String email;
    @Schema(description = "User password",
            example = "Qwerty12345678$")
    @NotBlank
    private String password;
    @Schema(description = "User confirmed password,"
            + " mast be similar with password",
            example = "Qwerty12345678$")
    @NotBlank
    private String confirmedPassword;
    @Schema(description = "User first name",
            example = "Bob")
    @NotBlank(message = "First name must not be empty")
    private String firstName;
    @Schema(description = "User last name",
            example = "Jonson")
    @NotBlank(message = "Last name must not be empty")
    private String lastName;
    @Schema(description = "User Shipping address",
            example = """
                    47 ANYVILLE RD
                    READING
                    BERKSHIRE
                    RG1 1AT
                    UNITED KINGDOM""")
    @Size(max = 255, message = "Description must not exceed 1000 characters")
    private String shippingAddress;
}
