package hive.pokedex.util;

public final class Validation {

  public static boolean isValid(final String text){
    try{
      return !text.isBlank();
    }catch (NullPointerException e){
      return false;
    }
  }
}
