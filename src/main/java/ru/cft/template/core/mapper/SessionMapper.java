package ru.cft.template.core.mapper;

import org.mapstruct.Mapper;
import ru.cft.template.api.dto.SessionDto;
import ru.cft.template.core.entity.Session;

import java.util.List;

@Mapper
public interface SessionMapper {
    List<SessionDto> map(List<Session> sessions);

    SessionDto map(Session session);
}
