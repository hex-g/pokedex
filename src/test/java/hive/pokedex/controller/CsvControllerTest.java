package hive.pokedex.controller;

import hive.pokedex.repository.PedagogueRepository;
import hive.pokedex.repository.StudentRepository;
import hive.pokedex.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsvControllerTest {
  private final String URL_SAVE_ALL_PEDAGOGUES = "/csv/saveAllPedagogues";
  private final String URL_SAVE_ALL_STUDENTS = "/csv/saveAllStudents";
  @Mock
  private PedagogueRepository pedagogueRepository;
  @Mock
  private StudentRepository studentRepository;
  @Mock
  private UserRepository userRepository;
  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    final var csvController =
        new CsvController(pedagogueRepository, studentRepository, userRepository);

    mockMvc = MockMvcBuilders.standaloneSetup(csvController).build();
  }

  @Test
  public void givenNoCsvIsProvided_whenPedagoguesAreSaved_then415IsReceived() throws Exception {

    mockMvc.perform(
        post(URL_SAVE_ALL_PEDAGOGUES)
    ).andExpect(status().isUnsupportedMediaType());
  }

  @Test
  public void givenCsvIsProvided_whenCsvIsEmptyOrOnlyWhiteSpaces_then400IsReceived() throws Exception {
    mockMvc.perform(
        post(URL_SAVE_ALL_PEDAGOGUES)
          .content("   ")
    ).andExpect(status().isBadRequest())
        .andExpect(status().reason("File is empty"));
  }

  @Test
  public void givenNoCsvIsProvided_whenStudentsAreSaved_then415IsReceived() throws Exception {

    mockMvc.perform(
        post(URL_SAVE_ALL_STUDENTS)
    ).andExpect(status().isUnsupportedMediaType());
  }
}
