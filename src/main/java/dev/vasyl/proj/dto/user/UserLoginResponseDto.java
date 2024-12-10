package dev.vasyl.proj.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginResponseDto {
    @Schema(description = "User jtw token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                    + "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4"
                    + "gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJ"
                    + "SMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private String token;
}
