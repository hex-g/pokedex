package hive.pokedex.controller;

import hive.pokedex.entity.Person;
import hive.pokedex.exception.EntityNotFoundException;
import hive.pokedex.repository.PersonRepository;
import hive.pokedex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static hive.pandora.constant.HiveInternalHeaders.AUTHENTICATED_USER_ID;

@RestController
@RequestMapping("/person")
public class PersonController {
  private final UserRepository userRepository;
  private final PersonRepository personRepository;

  @Autowired
  public PersonController(
      final UserRepository userRepository,
      final PersonRepository personRepository
  ) {
    this.userRepository = userRepository;
    this.personRepository = personRepository;
  }

  @GetMapping
  public Person find(
      @RequestHeader(name = AUTHENTICATED_USER_ID) final String userId
  ) {
    if(!userRepository.existsById(Integer.parseInt(userId))){
      throw new EntityNotFoundException();
    }

    final var person = new Person();
    person.setUser(userRepository.getOne(Integer.parseInt(userId)));

    return personRepository.findAll(Example.of(person)).get(0);
  }
}
