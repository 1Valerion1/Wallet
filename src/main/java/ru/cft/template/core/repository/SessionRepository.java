package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cft.template.core.entity.Sessions;
import ru.cft.template.core.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Sessions, String> {
    Sessions findByValue(String value);

    List<Sessions> findAllByUserAndActiveTrueAndExpirationTimeAfter(User user, LocalDateTime expirationTime);

    @Modifying
    @Query(value = "update sessions set active = false where expiration_time < :localDateTime",
            nativeQuery = true)
    void updateAllByExpirationTimeAfter(LocalDateTime localDateTime);

    List<Sessions> findAllByUserAndActiveTrue(User user);

    @Modifying
    @Query(value = "delete from sessions where active = false",
            nativeQuery = true)
    void deleteAllByExpirationTimeAfter();

}
