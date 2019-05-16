package hive.pokedex.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public final class FillerOfNullValues {
  public static void copyProperties(final Object source, final Object destiny) {
    BeanUtils.copyProperties(source, destiny, getNotNullAttributes(destiny));
  }

  private static String[] getNotNullAttributes(Object destiny) {
    final var source = new BeanWrapperImpl(destiny);
    final PropertyDescriptor[] attributes = source.getPropertyDescriptors();

    final Set<String> emptyNames = new HashSet();
    for (final PropertyDescriptor attribute : attributes) {
      final var attributeValue = source.getPropertyValue(attribute.getName());
      if (attributeValue != null && !attributeValue.toString().trim().isBlank()) {
        emptyNames.add(attribute.getName());
      }
    }
    return emptyNames.toArray(new String[emptyNames.size()]);
  }
}
