package br.com.wevertontsousa.mapstruct_lib;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class Main {

  public static void main(String[] args) {
    User user = new User(1L, "wever", "123", "2025-01-01");
    UserResponse response = MapStruct.INSTANCE.toUserResponse(user);
    System.out.println(response);
  }

}


@Getter
@AllArgsConstructor
class User {
    private long id;
    private String username;
    private String password;
    private String createdAt;
}


record UserResponse(long code, String username, String createdAt, String updatedAt) {
}


@Mapper // @Mapper(componentModel = "spring") // No Spring
interface MapStruct {
  MapStruct INSTANCE = Mappers.getMapper(MapStruct.class); // No Spring não precisa, usar injeção

  @Mapping(source = "id", target = "code")
  @Mapping(target = "updatedAt", ignore = true)
  UserResponse toUserResponse(User user);
}
