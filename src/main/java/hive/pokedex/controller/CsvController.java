package hive.pokedex.controller;

import hive.entity.user.Pedagogue;
import hive.entity.user.Person;
import hive.entity.user.Student;
import hive.entity.user.User;
import hive.pokedex.exception.EntityAlreadyExistsException;
import hive.pokedex.exception.FileException;
import hive.pokedex.exception.NullValueException;
import hive.pokedex.exception.UsernameAlreadyExistsException;
import hive.pokedex.repository.PedagogueRepository;
import hive.pokedex.repository.StudentRepository;
import hive.pokedex.repository.UserRepository;
import hive.pokedex.util.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hive.pokedex.util.Validation.isValid;

@RestController
@RequestMapping("/admin/csv")
public class CsvController {

  private final String PEDAGOGUE = "PEDAGOGUE";
  private final String STUDENT = "STUDENT";

  @Autowired
  private PedagogueRepository pedagogueRepository;
  @Autowired
  private StudentRepository studentRepository;
  @Autowired
  private UserRepository userRepository;


  @PostMapping(path = "/saveAllPedagogues", consumes = "multipart/form-data")
  public void saveAllPedagogues(@RequestParam("file") final MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new FileException();
    }

    try (final var br = new BufferedReader(new StringReader(new String(file.getBytes())))) {
      final var list = new ArrayList<Pedagogue>();

      for (final String[] row : new CSVParser(br.lines(), ';', '\"').parser().get()) {
        final var pedagogue = new Pedagogue(row[1]);

        final var person = new Person(row[0]);
        person.setUser(new User(row[2], row[3], PEDAGOGUE));

        pedagogue.setPerson(person);

        if (!isValid(row[0]) ||
            !isValid(row[1]) ||
            !isValid(row[2]) ||
            !isValid(row[3])) {
          throw new NullValueException();
        } else if (pedagogueRepository.existsByRm(row[1])) {
          throw new EntityAlreadyExistsException();
        } else if (userRepository.existsByUsername(row[2])) {
          throw new UsernameAlreadyExistsException();
        }

        list.add(pedagogue);
      }

      pedagogueRepository.saveAll(list);

    }

  }


  @PostMapping(path = "/saveAllStudents", consumes = "multipart/form-data")
  public void saveAllStudents(@RequestParam("file") final MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new FileException();
    }

    try (final var br = new BufferedReader(new StringReader(new String(file.getBytes())))) {
      final var list = new ArrayList<Student>();

      for (final String[] row : new CSVParser(br.lines(), ';', '\"').parser().get()) {
        final var student = new Student(row[1]);

        final var person = new Person(row[0]);
        person.setUser(new User(row[2], row[3], STUDENT));

        student.setPerson(person);

        if (!isValid(row[0]) ||
            !isValid(row[1]) ||
            !isValid(row[2]) ||
            !isValid(row[3])) {
          throw new NullValueException();
        } else if (studentRepository.existsByRa(row[1])) {
          throw new EntityAlreadyExistsException();
        } else if (userRepository.existsByUsername(row[2])) {
          throw new UsernameAlreadyExistsException();
        }

        list.add(student);
      }

      studentRepository.saveAll(list);

    }

  }


}
