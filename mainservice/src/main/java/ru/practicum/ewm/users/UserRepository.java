package ru.practicum.ewm.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.users.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from USERS where USER_ID in ?1 limit ?3 offset ?2", nativeQuery = true)
    List<User> findAllByIdsWithPagination(List<Long> ids, int from, int size);
}
