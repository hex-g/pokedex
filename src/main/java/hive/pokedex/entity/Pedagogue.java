package hive.pokedex.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicUpdate
@Entity
@Table(name = "tb_pedagogue")
public class Pedagogue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "rm", unique = true)
  private String rm;
  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "person_id", unique = true)
  private Person person;

  public Pedagogue() {
  }

  public Pedagogue(final String rm) {
    this.rm = rm;
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(final Person person) {
    this.person = person;
  }

  public String getRm() {
    return rm;
  }

  public void setRm(final String rm) {
    this.rm = rm;
  }
}
