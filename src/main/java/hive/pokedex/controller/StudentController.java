package hive.pokedex.controller;

import hive.ishigami.entity.user.Person;
import hive.ishigami.entity.user.Student;
import hive.ishigami.entity.user.User;
import hive.pokedex.exception.EntityAlreadyExistsException;
import hive.pokedex.exception.EntityNotFoundException;
import hive.pokedex.exception.NullValueException;
import hive.pokedex.exception.UsernameAlreadyExistsException;
import hive.pokedex.repository.StudentRepository;
import hive.pokedex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hive.pokedex.util.FillNullValues.copyProperties;
import static hive.pokedex.util.Validation.isValid;

@RestController
@RequestMapping("/student")
public class StudentController {
  private final String ROLE = "STUDENT";
  private final StudentRepository studentRepository;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Autowired
  public StudentController(
      final StudentRepository studentRepository,
      final UserRepository userRepository
  ) {
    this.studentRepository = studentRepository;
    this.userRepository = userRepository;
  }

  @GetMapping
  public List<Student> find(
      @RequestParam(required = false) final Integer id,
      @RequestParam(required = false) final String name,
      @RequestParam(required = false) final String ra,
      @RequestParam(required = false) final String username
  ) {

    final var student = new Student(ra);
    student.setId(id);

    final var person = new Person(name);
    person.setUser(new User(username, null, ROLE));

    student.setPerson(person);

    final var foundStudents = studentRepository.findAll(Example.of(student));

    if (foundStudents.isEmpty()) {
      throw new EntityNotFoundException();
    }

    return foundStudents;
  }

  @PostMapping
  public Student save(
      @RequestParam(required = false) final Integer id,
      @RequestParam(required = false) final String name,
      @RequestParam(required = false) final String ra,
      @RequestParam(required = false) final String username,
      @RequestParam(required = false) final String password
  ) {

    final var student = new Student(ra);
    final var person = new Person(name);

    final var user = new User(username, password, ROLE);
    person.setUser(user);

    student.setPerson(person);

    if (id != null) {
      if (!studentRepository.existsById(id)) {
        throw new EntityNotFoundException();
      }

      final var studentPersisted = studentRepository.getOne(id);

      copyProperties(student, studentPersisted);

      copyProperties(
          student.getPerson(),
          studentPersisted.getPerson()
      );

      copyProperties(
          student.getPerson().getUser(),
          studentPersisted.getPerson().getUser()
      );
    }

    if (!isValid(student.getRa()) ||
        !isValid(student.getPerson().getName()) ||
        !isValid(user.getUsername()) ||
        !isValid(user.getPassword())) {
      throw new NullValueException();
    }

    if (studentRepository.existsByRa(ra)) {
      throw new EntityAlreadyExistsException();
    }

    if (userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyExistsException();
    }

    user.setPassword(encoder.encode(user.getPassword()));
    studentRepository.save(student);

    return student;
  }

  @DeleteMapping
  public void deleteById(@RequestParam final int id) {

    if (!studentRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }

    studentRepository.deleteById(id);
  }
}
