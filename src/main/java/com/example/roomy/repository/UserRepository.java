package com.example.roomy.repository;

import com.example.roomy.model.User;
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
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByIdIn(List<Long> userIds);

    @Query("""
            select u from User u
            left join fetch u.roles r
            where (:search is null or u.username ilike 
                concat('%', cast(:search as string), '%'))
            and (:ids is null or u.id in :ids)
            and (:usernames is null or u.username in :usernames)
            and (:roles is null or r.name in :roles)
            """)
    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly", value = "true")
    })
    Page<User> findAllUsers(
            @Param("search") String search,
            @Param("ids") List<Long> ids,
            @Param("usernames") List<String> usernames,
            @Param("roles") List<String> roles,
            Pageable pageable
    );

}
