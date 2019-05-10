package hive.pokedex.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hive.ishigami.entity.user.Pedagogue;
import hive.ishigami.entity.user.Person;
import hive.ishigami.entity.user.User;
import hive.pokedex.repository.PedagogueRepository;
import hive.pokedex.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PedagogueControllerTest {
  private final Type type = new TypeToken<List<Pedagogue>>() {
  }.getType();
  private final String ROLE = "PEDAGOGUE";
  private final String URL = "/pedagogue";
  @Mock
  private PedagogueRepository pedagogueRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private BCryptPasswordEncoder encoder;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    final var pedagogueController = new PedagogueController(pedagogueRepository, userRepository, encoder);

    mockMvc = MockMvcBuilders.standaloneSetup(pedagogueController).build();
  }

  @Test
  public void givenPedagogueDoesNotExists_whenPedagogueInfoIsRetrieved_then404IsReceived() throws Exception {

    mockMvc.perform(
        get(URL)
            .param("username", "test")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void givenPedagogueExists_whenPedagogueInfoIsRetrieved_then200IsReceived() throws Exception {

    final List<Pedagogue> pedagogueList = new ArrayList<>();
    pedagogueList.add(new Pedagogue("rm-select-test"));

    when(pedagogueRepository.findAll((Example<Pedagogue>) any())).thenReturn(pedagogueList);

    final var result = mockMvc.perform(
        get(URL)
    ).andExpect(status().isOk()).andReturn();

    final List<Pedagogue> resultList =
        new Gson().fromJson(result.getResponse().getContentAsString(), type);

    assertEquals(resultList.get(0).getRm(), pedagogueList.get(0).getRm());
  }

  @Test
  public void givenPedagogueDoesNotExists_whenPedagogueUpdatedInfoIsProvided_then404IsReceived() throws Exception {

    mockMvc.perform(
        post(URL)
            .param("id", "3123")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));
  }

  @Test
  public void givenPedagogueExists_whenPedagogueUpdatedInfoIsProvided_then200IsReceived() throws Exception {
    when(pedagogueRepository.existsById(1)).thenReturn(true);

    final var pedagogue = new Pedagogue("rm");
    pedagogue.setId(1);

    final var person = new Person("test-updated");
    person.setUser(new User("test", "123", ROLE));

    pedagogue.setPerson(person);

    when(pedagogueRepository.getOne(1)).thenReturn(pedagogue);

    mockMvc.perform(
        post(URL)
            .param("id", "1")
            .param("name", "name-updated")
            .param("rm", "rm-updated")
            .param("username", "username-updated")
            .param("password", "password-updated")
    ).andExpect(status().isOk());
  }

  @Test
  public void givenNoPedagogueInfoIsProvided_whenPedagogueIsSaved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post(URL)
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));
  }

  @Test
  public void givenPedagogueInfoProvidedIsEmpty_whenPedagogueIsSaved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post(URL)
            .param("name", "")
            .param("rm", "")
            .param("username", "")
            .param("password", "")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));
  }

  @Test
  public void givenPedagogueInfoOnlyWhiteSpacesIsProvided_whenPedagogueIsSaved_then406IsReceived() throws Exception {

    mockMvc.perform(
        post(URL)
            .param("name", " ")
            .param("rm", " ")
            .param("username", "  ")
            .param("password", "  ")
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));
  }

  @Test
  public void givenRmAlreadyExists_whenPedagogueIsSaved_then409IsReceived() throws Exception {
    when(pedagogueRepository.existsByRm("rm-test")).thenReturn(true);

    mockMvc.perform(
        post(URL)
            .param("name", "test")
            .param("rm", "rm-test")
            .param("username", "test")
            .param("password", "test")
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Entity already registered"));
  }

  @Test
  public void givenUsernameAlreadyExists_whenPedagogueIsSaved_then409IsReceived() throws Exception {
    when(userRepository.existsByUsername("test")).thenReturn(true);

    mockMvc.perform(
        post(URL)
            .param("name", "test")
            .param("rm", "rm-test")
            .param("username", "test")
            .param("password", "test")
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Username already registered"));
  }

  @Test
  public void givenPedagogueDoesNotExists_whenDeletePedagogueById_then404IsReceived() throws Exception {

    mockMvc.perform(
        delete(URL)
            .param("id", "1")
    ).andExpect(status().isNotFound())
        .andExpect(status().reason("Entity not found"));
  }

  @Test
  public void givenPedagogueExists_whenDeletePedagogueById_then200IsReceived() throws Exception {
    when(pedagogueRepository.existsById(1)).thenReturn(true);

    mockMvc.perform(
        delete(URL)
            .param("id", "1")
    ).andExpect(status().isOk());
  }
}
