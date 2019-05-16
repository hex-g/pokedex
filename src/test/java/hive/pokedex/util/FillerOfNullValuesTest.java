package hive.pokedex.util;

import hive.ishigami.entity.user.Pedagogue;
import hive.ishigami.entity.user.Person;
import hive.ishigami.entity.user.User;
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

    copyProperties(destinyPedagogue, originPedagogue);

    copyProperties(
        destinyPedagogue.getPerson(),
        originPedagogue.getPerson()
    );

    copyProperties(
        destinyPedagogue.getPerson().getUser(),
        originPedagogue.getPerson().getUser()
    );

    boolean filledTheGaps = true;

    final var destinyPedagoguePerson = destinyPedagogue.getPerson();
    final var originPedagoguePerson = originPedagogue.getPerson();

    final var destinyPedagogueUser = destinyPedagogue.getPerson().getUser();
    final var originPedagogueUser = originPedagogue.getPerson().getUser();

    if (destinyPedagogue.getRm().equals(originPedagogue) ||
        !destinyPedagoguePerson.getId().equals(originPedagoguePerson.getId()) ||
        destinyPedagoguePerson.getName().equals(originPedagoguePerson.getName()) ||
        !destinyPedagogueUser.getId().equals(originPedagogueUser.getId()) ||
        !destinyPedagogueUser.getUsername().equals(originPedagogueUser.getUsername()) ||
        !destinyPedagogueUser.getPassword().equals(originPedagogueUser.getPassword()) ||
        !destinyPedagogueUser.getRole().equals(originPedagogueUser.getRole())
    ) {
      filledTheGaps = false;
    }

    assertTrue(filledTheGaps);
  }
}
