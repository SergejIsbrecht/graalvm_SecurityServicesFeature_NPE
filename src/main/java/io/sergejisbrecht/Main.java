package io.sergejisbrecht;

import java.security.Provider;
import java.util.UUID;
import org.conscrypt.Conscrypt;

public class Main {
  public static void main(String[] args) throws Exception {
    UUID uuid = UUID.randomUUID();
    System.out.println(uuid);
    Provider provider = Conscrypt.newProvider();
    System.out.println(provider);
    System.out.println("init");
  }
}
