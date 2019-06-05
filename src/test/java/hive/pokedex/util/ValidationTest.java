package hive.pokedex.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static hive.pokedex.util.Validation.isValid;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationTest {
  @Test
  public void verifyText_whenOnlyWhiteSpaces_returnFalse() {
    assertFalse(isValid("    "));
  }

  @Test
  public void verifyText_whenNull_returnFalse() {
    assertFalse(isValid(null));
  }

  @Test
  public void verifyText_whenTextIsRetrieved_returnTrue() {
    assertTrue(isValid("text-test"));
  }
}
