package com.example.roomy.model;

import com.example.roomy.enums.RoomStatus;
import com.example.roomy.enums.converter.RoomStatusConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rooms", schema = "room")
public class Room extends AbstractTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Convert(converter = RoomStatusConverter.class)
    @Column(name = "status")
    private RoomStatus status = RoomStatus.BACKLOG;

    @OneToOne
    @JoinColumn(name = "room_master_id", referencedColumnName = "user_id")
    @JsonIgnoreProperties("rooms")
    private User roomMaster;

    @ManyToMany
    @JoinTable(
            name = "room_members",
            schema = "room",
            joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "user_id")
    )
    @JsonIgnoreProperties("rooms")
    private Set<User> roomMembers = new HashSet<>();


    public void addRoomMember(User member) {
        roomMembers.add(member);
        member.getRooms().add(this);
    }

    public void removeRoomMember(User member) {
        roomMembers.remove(member);
        member.getRooms().remove(this);
    }

}
