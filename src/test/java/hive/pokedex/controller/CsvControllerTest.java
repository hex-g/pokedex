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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
  private MockMultipartFile multipartFile;

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
  public void givenPedagogueCsvIsProvided_whenCsvIsEmptyOrOnlyWhiteSpaces_then415IsReceived() throws Exception {
    mockMvc.perform(
        post(URL_SAVE_ALL_PEDAGOGUES)
            .content("   ")
    ).andExpect(status().isUnsupportedMediaType());
  }

  @Test
  public void givenPedagoguesInfoIsProvided_whenPedagogueIsSaved_then200IsReceived() throws Exception {

    final var pedagogues =
        "samuel,rm-01,samuel.gomes,123\nvictor,rm-02,victor.hirumitsu,321";

    multipartFile = new MockMultipartFile(
        "file", "test.csv", "text/csv",
        pedagogues.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
        multipart(URL_SAVE_ALL_PEDAGOGUES)
            .file(multipartFile)
    ).andExpect(status().isOk());
  }

  @Test
  public void givenNoPedagogueInfoIsProvided_whenPedagogueIsSaved_then406IsReceived() throws Exception {

    final var pedagogue = "name, , ,password";
    multipartFile = new MockMultipartFile(
        "file", "test.csv", "text/csv",
        pedagogue.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
        multipart(URL_SAVE_ALL_PEDAGOGUES)
            .file(multipartFile)
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenRmAlreadyExists_whenPedagogueIsSaved_then409IsReceived() throws Exception {
    when(pedagogueRepository.existsByRm("rm-01")).thenReturn(true);

    final var pedagogue = "samuel,rm-01,samuel.gomes,123";

    multipartFile = new MockMultipartFile(
        "file", "test.csv", "text/csv",
        pedagogue.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
        multipart(URL_SAVE_ALL_PEDAGOGUES)
            .file(multipartFile)
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Entity already registered"));
  }

  @Test
  public void givenUsernameAlreadyExists_whenPedagogueIsSaved_then409IsReceived() throws Exception {
    when(userRepository.existsByUsername("samuel.gomes")).thenReturn(true);

    final var pedagogue = "samuel,rm-01,samuel.gomes,123";

    multipartFile = new MockMultipartFile(
        "file", "test.csv", "text/csv",
        pedagogue.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
        multipart(URL_SAVE_ALL_PEDAGOGUES)
            .file(multipartFile)
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Username already registered"));
  }

  @Test
  public void givenNoCsvIsProvided_whenStudentsAreSaved_then415IsReceived() throws Exception {
    mockMvc.perform(
        post(URL_SAVE_ALL_STUDENTS)
    ).andExpect(status().isUnsupportedMediaType());
  }

  @Test
  public void givenStudentCsvIsProvided_whenCsvIsEmptyOrOnlyWhiteSpaces_then415IsReceived() throws Exception {
    mockMvc.perform(
        post(URL_SAVE_ALL_STUDENTS)
            .content("   ")
    ).andExpect(status().isUnsupportedMediaType());
  }

  @Test
  public void givenStudentInfoIsProvided_whenPedagogueIsSaved_then200IsReceived() throws Exception {

    final var student =
        "samuel,ra-01,samuel.gomes,123\nvictor,ra-02,victor.hirumitsu,321";

    multipartFile = new MockMultipartFile(
        "file", "test.csv", "text/csv",
        student.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
        multipart(URL_SAVE_ALL_STUDENTS)
            .file(multipartFile)
    ).andExpect(status().isOk());
  }

  @Test
  public void givenNoStudentInfoIsProvided_whenPedagogueIsSaved_then406IsReceived() throws Exception {

    final var student = "name, , ,password";
    multipartFile = new MockMultipartFile(
        "file", "test.csv", "text/csv",
        student.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
        multipart(URL_SAVE_ALL_STUDENTS)
            .file(multipartFile)
    ).andExpect(status().isNotAcceptable())
        .andExpect(status().reason("Null value not allowed"));

  }

  @Test
  public void givenRmAlreadyExists_whenStudentIsSaved_then409IsReceived() throws Exception {
    when(studentRepository.existsByRa("ra-02")).thenReturn(true);

    final var student = "victor,ra-02,victor.hirumitsu,321";

    multipartFile = new MockMultipartFile(
        "file", "test.csv", "text/csv",
        student.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
        multipart(URL_SAVE_ALL_STUDENTS)
            .file(multipartFile)
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Entity already registered"));
  }

  @Test
  public void givenUsernameAlreadyExists_whenStudentIsSaved_then409IsReceived() throws Exception {
    when(userRepository.existsByUsername("victor.hirumitsu")).thenReturn(true);

    final var student = "victor,ra-02,victor.hirumitsu,321";

    multipartFile = new MockMultipartFile(
        "file", "test.csv", "text/csv",
        student.getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(
        multipart(URL_SAVE_ALL_STUDENTS)
            .file(multipartFile)
    ).andExpect(status().isConflict())
        .andExpect(status().reason("Username already registered"));
  }
}
