package br.com.wevertontsousa.lombok_lib;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Remove o construtor padrão
@Data // `@Getter | @Setter | @RequiredArgsConstructor | @ToString | @EqualsAndHashCode
public class UserLombok {
  private String username;
}
