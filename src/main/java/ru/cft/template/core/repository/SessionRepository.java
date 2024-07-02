package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cft.template.core.entity.Session;
import ru.cft.template.core.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Session findByValue(String value);

    List<Session> findAllByUserAndActiveTrueAndExpirationTimeAfter(User user, LocalDateTime expirationTime);

    @Modifying
    @Query(value = "update session set active = false where expiration_time < :localDateTime",
            nativeQuery = true)
    void updateAllByExpirationTimeAfter(LocalDateTime localDateTime);
}
