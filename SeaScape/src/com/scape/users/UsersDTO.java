package com.scape.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UsersDTO {
    private String USER_ID;
    private String USER_PW;
    private String USER_NAME;
    private String USER_NICKNAME;
    private int USER_AGE;
    private int ESCAPE_HISTORY;
}
