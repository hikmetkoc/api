package tr.com.meteor.crm.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tr.com.meteor.crm.service.dto.UserDTO;
import tr.com.meteor.crm.domain.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {

    @Mapping(source = "createdBy", target = "createdBy.id")
    @Mapping(source = "lastModifiedBy", target = "lastModifiedBy.id")
    User toEntity(UserDTO dto);

    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "lastModifiedBy.id", target = "lastModifiedBy")
    UserDTO toDto(User entity);

    default UserDTO userDtoFromId(Long id) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(id);

        return userDTO;
    }

    default User userFromId(Long id) {
        User user = new User();

        user.setId(id);

        return user;
    }
}
