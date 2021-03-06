package hive.pokedex.controller;

import hive.pokedex.entity.Pedagogue;
import hive.pokedex.entity.Person;
import hive.pokedex.entity.Student;
import hive.pokedex.entity.User;
import hive.pokedex.exception.EntityAlreadyExistsException;
import hive.pokedex.exception.NullValueException;
import hive.pokedex.exception.UsernameAlreadyExistsException;
import hive.pokedex.repository.PedagogueRepository;
import hive.pokedex.repository.StudentRepository;
import hive.pokedex.repository.UserRepository;
import hive.pokedex.util.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static hive.pokedex.util.Validation.isValid;

@RestController
@RequestMapping("/csv")
public class CsvController {
  private final String PEDAGOGUE = "PEDAGOGUE";
  private final String STUDENT = "STUDENT";
  private final PedagogueRepository pedagogueRepository;
  private final StudentRepository studentRepository;
  private final UserRepository userRepository;

  @Autowired
  public CsvController(
      final PedagogueRepository pedagogueRepository,
      final StudentRepository studentRepository,
      final UserRepository userRepository
  ) {
    this.pedagogueRepository = pedagogueRepository;
    this.studentRepository = studentRepository;
    this.userRepository = userRepository;
  }

  @GetMapping("/exportAllPedagogues")
  public StringBuilder exportAllPedagogues() {
    final var csv = new StringBuilder();

    for (final var pedagogue : pedagogueRepository.findAll()) {
      csv.append(pedagogue.getPerson().getFirstName() + ",");
      csv.append(pedagogue.getPerson().getLastName() + ",");
      csv.append(pedagogue.getRm() + ",");
      csv.append(pedagogue.getPerson().getUser().getUsername() + ",");
      csv.append(pedagogue.getPerson().getUser().getPassword() + "\n");
    }

    return csv;
  }

  @GetMapping("/exportAllStudents")
  public StringBuilder exportAllStudents() {
    final var csv = new StringBuilder();

    for (final var student : studentRepository.findAll()) {
      csv.append(student.getPerson().getFirstName() + ",");
      csv.append(student.getPerson().getLastName() + ",");
      csv.append(student.getRa() + ",");
      csv.append(student.getPerson().getUser().getUsername() + ",");
      csv.append(student.getPerson().getUser().getPassword() + "\n");
    }

    return csv;
  }

  @PostMapping(value = "/saveAllPedagogues", consumes = "multipart/form-data")
  public Integer saveAllPedagogues(
      @RequestParam("file") final MultipartFile file
  ) throws IOException {
    try (final var br = new BufferedReader(new StringReader(new String(file.getBytes())))) {
      final var list = new ArrayList<Pedagogue>();

      for (final String[] row : new CSVParser(br.lines(), ',', '\"').parser().get()) {

        final var pedagogue = new Pedagogue(row[2]);

        final var person = new Person(row[0], row[1]);
        person.setUser(new User(row[3], row[4], PEDAGOGUE));

        pedagogue.setPerson(person);

        if (!isValid(row[0]) ||
            !isValid(row[1]) ||
            !isValid(row[2]) ||
            !isValid(row[3]) ||
            !isValid(row[4])) {
          throw new NullValueException();
        } else if (pedagogueRepository.existsByRm(row[2])) {
          throw new EntityAlreadyExistsException();
        } else if (userRepository.existsByUsername(row[3])) {
          throw new UsernameAlreadyExistsException();
        }

        list.add(pedagogue);
      }

      return pedagogueRepository.saveAll(list).size();
    }
  }

  @PostMapping(value = "/saveAllStudents", consumes = "multipart/form-data")
  public Integer saveAllStudents(
      @RequestParam("file") final MultipartFile file
  ) throws IOException {
    try (final var br = new BufferedReader(new StringReader(new String(file.getBytes())))) {
      final var list = new ArrayList<Student>();

      for (final String[] row : new CSVParser(br.lines(), ',', '\"').parser().get()) {
        final var student = new Student(row[2]);

        final var person = new Person(row[0], row[1]);
        person.setUser(new User(row[3], row[4], STUDENT));

        student.setPerson(person);

        if (!isValid(row[0]) ||
            !isValid(row[1]) ||
            !isValid(row[2]) ||
            !isValid(row[3]) ||
            !isValid(row[4])) {
          throw new NullValueException();
        } else if (studentRepository.existsByRa(row[2])) {
          throw new EntityAlreadyExistsException();
        } else if (userRepository.existsByUsername(row[3])) {
          throw new UsernameAlreadyExistsException();
        }

        list.add(student);
      }

      return studentRepository.saveAll(list).size();
    }
  }
}
