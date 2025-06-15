package com.example.roomy.repository;

import com.example.roomy.model.Room;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByName(String name);

    boolean existsByName(String name);


    @Query("""
            select r from Room r
            left join fetch r.roomMaster rm
            left join fetch r.roomMembers rms
            where (:search is null or r.name ilike 
                    concat('%', cast(:search as string), '%'))
                or (:search is null or rm.username ilike
                    concat('%', cast(:search as string), '%'))
                or (:search is null or rms.username ilike
                    concat('%', cast(:search as string), '%'))
                and (:ids is null or r.id in :ids)
                and (:names is null or r.name in :names)
                and (:statuses is null or r.status in :statuses)
                and (:roomMasterIds is null or rm.id in :roomMasterIds)
                and (:roomMemberIds is null or rms.id in :roomMemberIds)            
            """)
    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly", value = "true")
    })
    Page<Room> findAllRooms(
            @Param("search") String search,
            @Param("ids") List<Long> ids,
            @Param("names") List<String> names,
            @Param("statuses") List<String> statuses,
            @Param("roomMasterIds") List<Long> roomMasterIds,
            @Param("roomMemberIds") List<Long> roomMemberIds,
            Pageable pageable
    );
}
