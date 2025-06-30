package br.com.wevertontsousa.lombok_lib;

import lombok.Builder;

@SuppressWarnings(value = "unused")
@Builder(builderClassName = "DogBuilder") // Regras de Negócio dunate o Builder
public class DogLombok {
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


  public static void main(String[] args) {
    try {
      DogLombok dog = DogLombok.builder().id(null).name("Caramelo").build();
      System.out.println(dog);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

}
