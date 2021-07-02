package pl.edu.wat.backend.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private Date birthDate;
    private int phoneNumber;
    private UUID token;
    private Boolean loggedIn ;
    @Column(unique = true)
    private String email;
    @OneToMany(mappedBy = "organizer")
    List<MeetingEntity> meetings;
    @OneToMany(mappedBy = "userInvited")
    List<InvitationEntity> usersInvitations;
}
