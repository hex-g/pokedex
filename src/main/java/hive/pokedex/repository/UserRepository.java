package hive.pokedex.repository;

import hive.ishigami.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
  boolean existsByUsername(String username);
}
