package tr.com.meteor.crm.service.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import tr.com.meteor.crm.domain.Role;
import tr.com.meteor.crm.domain.User;
import tr.com.meteor.crm.service.dto.UserDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-13T11:28:52+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_202 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public List<User> toEntity(List<UserDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( dtoList.size() );
        for ( UserDTO userDTO : dtoList ) {
            list.add( toEntity( userDTO ) );
        }

        return list;
    }

    @Override
    public List<UserDTO> toDto(List<User> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( entityList.size() );
        for ( User user : entityList ) {
            list.add( toDto( user ) );
        }

        return list;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setCreatedBy( userDTOToUser( dto ) );
        user.setLastModifiedBy( userDTOToUser1( dto ) );
        user.setId( dto.getId() );
        user.setCreatedDate( dto.getCreatedDate() );
        user.setLastModifiedDate( dto.getLastModifiedDate() );
        user.setLogin( dto.getLogin() );
        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );
        user.setEmail( dto.getEmail() );
        user.setImageUrl( dto.getImageUrl() );
        user.setActivated( dto.isActivated() );
        user.setLangKey( dto.getLangKey() );
        Set<Role> set = dto.getRoles();
        if ( set != null ) {
            user.setRoles( new HashSet<Role>( set ) );
        }

        return user;
    }

    @Override
    public UserDTO toDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setCreatedBy( entityCreatedById( entity ) );
        userDTO.setLastModifiedBy( entityLastModifiedById( entity ) );
        userDTO.setId( entity.getId() );
        userDTO.setLogin( entity.getLogin() );
        userDTO.setFirstName( entity.getFirstName() );
        userDTO.setLastName( entity.getLastName() );
        userDTO.setEmail( entity.getEmail() );
        userDTO.setImageUrl( entity.getImageUrl() );
        if ( entity.getActivated() != null ) {
            userDTO.setActivated( entity.getActivated() );
        }
        userDTO.setLangKey( entity.getLangKey() );
        userDTO.setCreatedDate( entity.getCreatedDate() );
        userDTO.setLastModifiedDate( entity.getLastModifiedDate() );
        Set<Role> set = entity.getRoles();
        if ( set != null ) {
            userDTO.setRoles( new HashSet<Role>( set ) );
        }

        return userDTO;
    }

    protected User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getCreatedBy() );

        return user;
    }

    protected User userDTOToUser1(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getLastModifiedBy() );

        return user;
    }

    private Long entityCreatedById(User user) {
        if ( user == null ) {
            return null;
        }
        User createdBy = user.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        Long id = createdBy.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityLastModifiedById(User user) {
        if ( user == null ) {
            return null;
        }
        User lastModifiedBy = user.getLastModifiedBy();
        if ( lastModifiedBy == null ) {
            return null;
        }
        Long id = lastModifiedBy.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
