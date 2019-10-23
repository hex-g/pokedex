package hive.pokedex.entity;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicUpdate
@Entity
@Table(name = "tb_user")
@Check(constraints = "role in ('STUDENT', 'PEDAGOGUE', 'ADMIN')")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "username", unique = true)
  private String username;
  @Column(name = "password")
  private String password;
  @Column(name = "role")
  private String role;

  public User() {
  }

  public User(final String username, final String password, final String role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(final String role) {
    this.role = role;
  }
}
