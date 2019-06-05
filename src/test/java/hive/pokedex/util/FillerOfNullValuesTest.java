package hive.pokedex.util;

import hive.pokedex.entity.Pedagogue;
import hive.pokedex.entity.Person;
import hive.pokedex.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static hive.pokedex.util.FillerOfNullValues.copyProperties;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FillerOfNullValuesTest {
  private final String ROLE = "PEDAGOGUE";

  @Test
  public void fillNullValueWithSourceValue_whenNullValueIsFound_expectObjectFill() {
    final var originPedagogue = new Pedagogue("rm-test-fill");
    originPedagogue.setId(1);

    final var person = new Person("name-test-fill");
    person.setId(1);

    final var user = new User("username-test-fill", "password-test-fill", ROLE);
    user.setId(1);

    person.setUser(user);

    originPedagogue.setPerson(person);

    final var destinyPedagogue = new Pedagogue("rm-test-new");
    destinyPedagogue.setId(1);

    final var destinyPerson = new Person("name-test-fill-new");
    destinyPerson.setUser(new User(null, "  ", ""));

    destinyPedagogue.setPerson(destinyPerson);

    copyProperties(originPedagogue, destinyPedagogue);

    copyProperties(
        originPedagogue.getPerson(),
        destinyPedagogue.getPerson()
    );

    copyProperties(
        originPedagogue.getPerson().getUser(),
        destinyPedagogue.getPerson().getUser()
    );

    boolean filledTheGaps = true;

    final var destinyPedagoguePerson = destinyPedagogue.getPerson();
    final var originPedagoguePerson = originPedagogue.getPerson();

    final var destinyPedagogueUser = destinyPedagogue.getPerson().getUser();
    final var originPedagogueUser = originPedagogue.getPerson().getUser();

    if (originPedagogue.getRm().equals(destinyPedagogue.getRm()) ||
        !originPedagoguePerson.getId().equals(destinyPedagoguePerson.getId()) ||
        originPedagoguePerson.getName().equals(destinyPedagoguePerson.getName()) ||
        !originPedagogueUser.getId().equals(destinyPedagogueUser.getId()) ||
        !originPedagogueUser.getUsername().equals(destinyPedagogueUser.getUsername()) ||
        !originPedagogueUser.getPassword().equals(destinyPedagogueUser.getPassword()) ||
        !originPedagogueUser.getRole().equals(destinyPedagogueUser.getRole())
    ) {
      filledTheGaps = false;
    }

    assertTrue(filledTheGaps);
  }
}
