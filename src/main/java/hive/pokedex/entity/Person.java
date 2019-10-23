package hive.pokedex.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicUpdate
@Entity
@Table(name = "tb_person")
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "firstName")
  private String firstName;
  @Column(name = "lastName")
  private String lastName;

  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "user_id", unique = true)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private User user;

  public Person() {
  }

  public Person(final String firstName, final String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public String toString() {
    return "Person{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", user=" + user +
        '}';
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
