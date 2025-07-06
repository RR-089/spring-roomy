package com.example.roomy.repository.specification;

import com.example.roomy.model.Room;
import com.example.roomy.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RoomSpecification {
    public static Specification<Room> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            Join<Room, User> masterJoin = root.join("roomMaster", JoinType.LEFT);
            Join<Room, User> membersJoin = root.joinSet("roomMembers", JoinType.LEFT);

            assert query != null;
            query.distinct(true);

            String likePattern = "%" + search.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), likePattern),
                    cb.like(cb.lower(masterJoin.get("username")), likePattern),
                    cb.like(cb.lower(membersJoin.get("username")), likePattern)
            );
        };
    }

    public static Specification<Room> withIds(List<Long> ids) {
        return (root, query, cb) ->
                (ids == null || ids.isEmpty()) ? null : root.get("id").in(ids);
    }

    public static Specification<Room> withNames(List<String> names) {
        return (root, query, cb) ->
                (names == null || names.isEmpty()) ? null : root.get("name").in(names);
    }

    public static Specification<Room> withStatuses(List<String> statuses) {
        return (root, query, cb) ->
                (statuses == null || statuses.isEmpty()) ? null :
                        root.get("status").in(statuses);
    }

    public static Specification<Room> withMasterIds(List<Long> masterIds) {
        return (root, query, cb) -> {
            if (masterIds == null || masterIds.isEmpty()) return null;

            Join<Room, User> masterJoin = root.join("roomMaster", JoinType.LEFT);

            return masterJoin.get("id").in(masterIds);
        };
    }

    public static Specification<Room> withMemberIds(List<Long> memberIds) {
        return (root, query, cb) -> {
            if (memberIds == null || memberIds.isEmpty()) return null;

            Join<Room, User> membersJoin = root.join("roomMembers", JoinType.LEFT);

            return membersJoin.get("id").in(memberIds);
        };
    }

    public static Specification<Room> withMemberUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank()) return null;

            Join<Room, User> membersJoin = root.join("roomMembers", JoinType.LEFT);

            return cb.equal(membersJoin.get("username"), username);
        };
    }
}
