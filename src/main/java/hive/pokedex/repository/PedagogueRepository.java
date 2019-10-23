package hive.pokedex.repository;

import hive.pokedex.entity.Pedagogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PedagogueRepository extends JpaRepository<Pedagogue, Integer> {
  boolean existsByRm(String rm);
}
