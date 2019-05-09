package hive.pokedex.controller;

import hive.ishigami.entity.user.Pedagogue;
import hive.ishigami.entity.user.Person;
import hive.ishigami.entity.user.User;
import hive.pokedex.exception.EntityAlreadyExistsException;
import hive.pokedex.exception.EntityNotFoundException;
import hive.pokedex.exception.NullValueException;
import hive.pokedex.exception.UsernameAlreadyExistsException;
import hive.pokedex.repository.PedagogueRepository;
import hive.pokedex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hive.pokedex.util.FillNullValues.copyProperties;
import static hive.pokedex.util.Validation.isValid;

@RestController
@RequestMapping("/pedagogue")
public class PedagogueController {
  private final String ROLE = "PEDAGOGUE";
  private final PedagogueRepository pedagogueRepository;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Autowired
  public PedagogueController(
      final PedagogueRepository pedagogueRepository,
      final UserRepository userRepository
  ) {
    this.pedagogueRepository = pedagogueRepository;
    this.userRepository = userRepository;
  }

  @GetMapping
  public List<Pedagogue> find(
      @RequestParam(required = false) final Integer id,
      @RequestParam(required = false) final String name,
      @RequestParam(required = false) final String rm,
      @RequestParam(required = false) final String username
  ) {

    final var pedagogue = new Pedagogue(rm);
    pedagogue.setId(id);

    final var person = new Person(name);
    person.setUser(new User(username, null, ROLE));

    pedagogue.setPerson(person);

    final var foundPedagogues = pedagogueRepository.findAll(Example.of(pedagogue));

    if (foundPedagogues.isEmpty()) {
      throw new EntityNotFoundException();
    }

    return foundPedagogues;
  }

  @PostMapping
  public Pedagogue save(
      @RequestParam(required = false) final Integer id,
      @RequestParam(required = false) final String name,
      @RequestParam(required = false) final String rm,
      @RequestParam(required = false) final String username,
      @RequestParam(required = false) final String password
  ) {

    final var pedagogue = new Pedagogue(rm);
    final var person = new Person(name);

    final var user = new User(username, password, ROLE);
    person.setUser(user);

    pedagogue.setPerson(person);

    if (id != null) {
      if (!pedagogueRepository.existsById(id)) {
        throw new EntityNotFoundException();
      }

      final var pedagoguePersisted = pedagogueRepository.getOne(id);

      copyProperties(pedagogue, pedagoguePersisted);

      copyProperties(
          pedagogue.getPerson(),
          pedagoguePersisted.getPerson()
      );

      copyProperties(
          pedagogue.getPerson().getUser(),
          pedagoguePersisted.getPerson().getUser()
      );
    }

    if (!isValid(pedagogue.getRm()) ||
        !isValid(pedagogue.getPerson().getName()) ||
        !isValid(user.getUsername()) ||
        !isValid(user.getPassword())) {
      throw new NullValueException();
    }

    if (pedagogueRepository.existsByRm(rm)) {
      throw new EntityAlreadyExistsException();
    }

    if (userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyExistsException();
    }

    user.setPassword(encoder.encode(user.getPassword()));
    pedagogueRepository.save(pedagogue);

    return pedagogue;
  }

  @DeleteMapping
  public void deleteById(@RequestParam final int id) {

    if (!pedagogueRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }

    pedagogueRepository.deleteById(id);
  }
}
