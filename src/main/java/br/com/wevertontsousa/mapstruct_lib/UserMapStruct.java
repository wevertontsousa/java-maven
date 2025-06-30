package br.com.wevertontsousa.mapstruct_lib;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMapStruct {
    private long id;
    private String username;
    private String password;
    private String createdAt;


    public record UserResponse(
      long code,
      String username,
      String createdAt,
      String updatedAt
    ) {
    }


    @Mapper // @Mapper(componentModel = "spring") // No Spring
    public interface MapStruct {
      MapStruct INSTANCE = Mappers.getMapper(MapStruct.class); // No Spring não precisa, usar injeção

      @Mapping(source = "id", target = "code")
      @Mapping(target = "updatedAt", ignore = true)
      UserResponse toUserResponse(UserMapStruct user);
    }


    public static void main(String[] args) {
      UserMapStruct user = new UserMapStruct(1L, "wever", "123", "2025-01-01");
      UserResponse response = MapStruct.INSTANCE.toUserResponse(user);
      System.out.println(response);
    }

}
