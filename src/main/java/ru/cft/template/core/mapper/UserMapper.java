package ru.cft.template.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.cft.template.api.dto.UserDto;
import ru.cft.template.api.dto.UserPatchRequest;
import ru.cft.template.core.entity.User;

import java.time.LocalDate;
import java.time.Period;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UserMapper {

    @Mapping(target = "age", source = "birthday", qualifiedByName = "mapAge")
    @Mapping(target = "lastUpdateDate", source = "updateDate")
    @Mapping(target = "registrationDate", source = "creationDate")
    UserDto map(User user);

    void map(@MappingTarget User user, UserPatchRequest userPatchRequest);

    @Named("mapAge")
    default int mapAge(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }
}
