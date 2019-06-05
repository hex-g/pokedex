package hive.pokedex.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicUpdate
@Entity
@Table(name = "tb_person")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  public Person() {
  }

  public Person(final String name) {
    this.name = name;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

}
