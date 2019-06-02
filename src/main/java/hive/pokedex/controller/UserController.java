package hive.pokedex.controller;

import hive.pokedex.entity.User;
import hive.pokedex.exception.EntityNotFoundException;
import hive.pokedex.exception.NullValueException;
import hive.pokedex.exception.UsernameAlreadyExistsException;
import hive.pokedex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hive.pokedex.util.FillerOfNullValues.copyProperties;
import static hive.pokedex.util.Validation.isValid;

@RestController
@RequestMapping("/user")
public class UserController {
  private final String ROLE = "ADMIN";
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;

  @Autowired
  public UserController(final UserRepository userRepository, final BCryptPasswordEncoder encoder) {
    this.userRepository = userRepository;
    this.encoder = encoder;
  }

  @GetMapping
  public List<User> find(
      @RequestParam(required = false) final Integer id,
      @RequestParam(required = false) final String username,
      @RequestParam(required = false) final String role
  ) {

    final var user = new User(username, null, role);
    user.setId(id);

    final var foundUsers = userRepository.findAll(Example.of(user));

    if (foundUsers.isEmpty()) {
      throw new EntityNotFoundException();
    }

    return foundUsers;
  }

  @PostMapping
  public User save(
      @RequestParam(required = false) final Integer id,
      @RequestParam(required = false) final String username,
      @RequestParam(required = false) final String password
  ) {

    final var user = new User(username, password, ROLE);

    if (id != null) {
      if (!userRepository.existsById(id)) {
        throw new EntityNotFoundException();
      }

      final var userPersisted = userRepository.getOne(id);

      copyProperties(userPersisted, user);
    }

    if (!isValid(user.getUsername()) ||
        !isValid(user.getPassword())) {
      throw new NullValueException();
    }

    if (userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyExistsException();
    }

    user.setPassword(encoder.encode(user.getPassword()));
    userRepository.save(user);

    return user;
  }

  @DeleteMapping
  public void deleteById(@RequestParam final Integer id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }

    userRepository.deleteById(id);
  }
}
