package me.hoseok.twitterdemo.account.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountUpdateReq {
    @NotBlank
    @Size(min = 4, max = 30)
    private String username;
    @NotBlank
    @Email
    private String email;
}
