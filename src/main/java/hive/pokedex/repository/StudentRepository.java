package hive.pokedex.repository;

import hive.pokedex.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StudentRepository extends JpaRepository<Student, Integer> {
  boolean existsByRa(String ra);
}
