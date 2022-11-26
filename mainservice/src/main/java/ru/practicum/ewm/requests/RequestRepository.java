package ru.practicum.ewm.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.requests.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query(value = "select count(*) from requests where EVENT_ID = ?1 and STATE ='CONFIRMED'", nativeQuery = true)
    int getConfirmed(Long id);

    @Query("select r from Request r where r.eventId = ?1")
    List<Request> findAllByEvent_id(Long id);

    @Query("select r from Request r where r.requestorId = ?1")
    List<Request> findAllByRequestor_id(Long id);
}
