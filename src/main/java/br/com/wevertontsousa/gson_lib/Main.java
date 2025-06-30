package br.com.wevertontsousa.gson_lib;

import com.google.gson.Gson;

public class Main {

  public static void main(String[] args) {
    Foo foo = new Foo("bar", 1);
    Gson gson = new Gson();
    String json = gson.toJson(foo);
    System.out.println(json);
  }

}


@SuppressWarnings(value = "unused")
class Foo {
  private String bar;
  private int baz;

  public Foo(String bar, int baz) {
    this.bar = bar;
    this.baz = baz;
  }

}
