package com.example.roomy.repository;

import com.example.roomy.model.Task;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            select t from Task t
            join fetch t.room r
            left join fetch t.assignees a
            where (:search is null or t.name
                ilike concat('%', cast(:search as string), '%'))
            or (:search is null or r.name
                ilike concat('%', cast(:search as string), '%'))
            and (:ids is null or t.id in :ids)
            and (:roomIds is null or r.id in :roomIds)
            and (:assigneeIds is null or a.id in :assigneeIds)
            and (:statuses is null or t.status in :statuses)
            and (:minAssignee is null or :maxAssignee is null
                or (t.maxAssignee >= :minAssignee
                    and t.maxAssignee <= :maxAssignee))
            and (:minDurationInMinutes is null or :maxDurationInMinutes is null
                or (t.durationInMinutes >= :minDurationInMinutes
                    and t.durationInMinutes <= :maxDurationInMinutes))
            and (coalesce(:minStartDate, t.startDate) = t.startDate
                or t.startDate >= :minStartDate)
            and (coalesce(:maxStartDate, t.startDate) = t.startDate
                or t.startDate <= :maxStartDate)
            and (coalesce(:minExpectedFinishedDate, t.expectedFinishedDate) = t.expectedFinishedDate
                or t.expectedFinishedDate >= :minExpectedFinishedDate)
            and (coalesce(:maxExpectedFinishedDate, t.expectedFinishedDate) = t.expectedFinishedDate
                or t.expectedFinishedDate <= :maxExpectedFinishedDate)
            and (coalesce(:minFinishedDate, t.finishedDate) = t.finishedDate
                or t.finishedDate >= :minFinishedDate)
            and (coalesce(:maxFinishedDate, t.finishedDate) = t.finishedDate
                or t.finishedDate <= :maxFinishedDate)
            """
    )
    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly", value = "true")
    })
    Page<Task> findAllTasks(
            @Param("search") String search,
            @Param("ids") List<Long> ids,
            @Param("roomIds") List<Long> roomIds,
            @Param("assigneeIds") List<Long> assigneeIds,
            @Param("statuses") List<String> statuses,
            @Param("minAssignee") Integer minAssignee,
            @Param("maxAssignee") Integer maxAssignee,
            @Param("minDurationInMinutes") Long minDurationInMinutes,
            @Param("maxDurationInMinutes") Long maxDurationInMinutes,
            @Param("minStartDate") LocalDateTime minStartDate,
            @Param("maxStartDate") LocalDateTime maxStartDate,
            @Param("minExpectedFinishedDate") LocalDateTime minExpectedFinishedDate,
            @Param("maxExpectedFinishedDate") LocalDateTime maxExpectedFinishedDate,
            @Param("minFinishedDate") LocalDateTime minFinishedDate,
            @Param("maxFinishedDate") LocalDateTime maxFinishedDate,
            Pageable pageable
    );
}
