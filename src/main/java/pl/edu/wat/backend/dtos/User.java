package pl.edu.wat.backend.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.wat.backend.jpa.InvitationEntity;
import pl.edu.wat.backend.jpa.MeetingEntity;


import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private int id;
    private String username;
    private String first_name;
    private String last_name;
    private int phone_number;
    private String email;
    private String password;
    private UUID token;
    private String birthDate;
    private List<MeetingEntity> meetings;
    private List<InvitationEntity> usersInvitations;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
