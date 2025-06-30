package br.com.wevertontsousa.lombok_lib;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

public class Main {

  public static void main(String[] args) {
    Foo foo = new Foo();
    foo.setBar("bar");
    System.out.println(foo);


    // `@AllArgsConstructor`
    ProductLombok product = new ProductLombok(1L, "blusa", List.of("p"), "999", 1000.00);
    System.out.println(product);

    // `@NoArgsConstructor` e `@Setter`
    product = new ProductLombok();
    product.setId(2L);
    product.setName(("bermuda"));
    product.setSizes(List.of("m"));
    System.out.println(product);

    // `@Builder`
    product = ProductLombok
      .builder()
      .id(3L)
      .name("camisa")
      .size("g")
      .size("gg")
      .size("xg")
      .build();
    System.out.println(product);


    try {
      DogLombok dog = DogLombok.builder().id(null).name("Caramelo").build();
      System.out.println(dog);
  
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

}


@RequiredArgsConstructor // Remove o construtor padrão
@Data // `@Getter | @Setter | @RequiredArgsConstructor | @ToString | @EqualsAndHashCode
class Foo {
  private String bar;
}


@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
class ProductLombok {
  private long id;
  private String name;

  @Singular(value = "size") // Gera um Builder para inserir elementos de forma individual
  private List<String> sizes;

  @ToString.Exclude // É removido do `.toString()`
  @Setter(value = AccessLevel.NONE) // É removido o `.setCpf()` e também do `@AllArgsConstructor`
  private String cpf;

  @Setter(value = AccessLevel.PRIVATE) // `private void setSalary()`
  private double salary;
}


@SuppressWarnings(value = "unused")
@Builder(builderClassName = "DogBuilder") // Regras de Negócio dunate o Builder
class DogLombok {
  private String id;
  private String name;

  public static class DogBuilder {
    public DogLombok build() {
      if (this.id == null) {
        throw new RuntimeException("O ID do Cão não pode ser nulo");
      }

      return new DogLombok(this.id, this.name);
    }
  }

}
