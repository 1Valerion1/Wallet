package ru.cft.template.core.mapper;

import org.mapstruct.Mapper;
import ru.cft.template.api.dto.SessionDto;
import ru.cft.template.core.entity.Sessions;

import java.util.List;

@Mapper
public interface SessionMapper {
    List<SessionDto> map(List<Sessions> sessions);

    SessionDto map(Sessions sessions);
}
