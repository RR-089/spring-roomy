package com.example.roomy.model;

import com.example.roomy.enums.TaskStatus;
import com.example.roomy.enums.converter.TaskStatusConverter;
import com.example.roomy.exception.BadRequestException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tasks", schema = "room")
public class Task extends AbstractTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "max_assignee")
    private Integer maxAssignee;

    @Column(name = "status")
    @Convert(converter = TaskStatusConverter.class)
    private TaskStatus status;

    @Column(name = "duration_in_minutes")
    private Long durationInMinutes;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "finished_date")
    private LocalDateTime finishedDate;

    @Column(name = "expected_start_date")
    private LocalDateTime expectedStartDate;

    @Column(name = "expected_finished_date")
    private LocalDateTime expectedFinishedDate;

    @ManyToOne
    @JoinColumn(
            name = "room_id",
            referencedColumnName = "room_id",
            nullable = false
    )
    private Room room;

    @ManyToMany
    @JoinTable(
            name = "task_assignees",
            schema = "room",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "assignee_id",
                    referencedColumnName = "user_id")
    )
    @JsonIgnoreProperties("tasks")
    private Set<User> assignees = new HashSet<>();

    public void addTaskAssignee(User user) {

        if (finishedDate != null && status == TaskStatus.FINISHED) {
            throw new BadRequestException("Finished tasks cannot have new assignees",
                    null);
        }

        assignees.add(user);
        user.getTasks().add(this);
    }

    public void removeTaskAssignee(User user) {
        if (finishedDate != null && status == TaskStatus.FINISHED) {
            throw new BadRequestException("A finished task cannot be deleted", null);
        }

        assignees.remove(user);
        user.getTasks().remove(this);
    }
}
