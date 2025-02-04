package vk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import vk.dto.IsMemberRequest;
import vk.utils.UserUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserUtils userUtils;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;


    @BeforeEach
    public void setUp() {
        token = jwt().jwt(builder -> builder.subject(userUtils.getTestUser().getEmail()));
    }
    @Test
    public void testNullBody() throws Exception {
        var request = post("/isMember")
                .header("vk_service_token", "123123124")
                .contentType(MediaType.APPLICATION_JSON)
                .with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Не передано тело запроса");
    }

    @Test
    public void testNullToken() throws Exception {
        var req = Instancio.of(IsMemberRequest.class)
                .supply(Select.field(IsMemberRequest::getUserId), () -> 121123)
                .supply(Select.field(IsMemberRequest::getGroupId), () -> "121123")
                .create();
        var request = post("/isMember")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req))
                .with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("vk_service_token не должен быть пустым");
    }

    @Test
    public void testWrongToken() throws Exception {
        var req = Instancio.of(IsMemberRequest.class)
                .supply(Select.field(IsMemberRequest::getUserId), () -> 121123)
                .supply(Select.field(IsMemberRequest::getGroupId), () -> "121124")
                .create();
        var request = post("/isMember")
                .header("vk_service_token", "123123124")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req))
                .with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("error");
    }

    @Test
    public void testToken() throws Exception {
        if (System.getenv("VK_TOKEN") != null) {
            var vkToken = System.getenv("VK_TOKEN");
            var req = Instancio.of(IsMemberRequest.class)
                    .supply(Select.field(IsMemberRequest::getUserId), () -> 19537439)
                    .supply(Select.field(IsMemberRequest::getGroupId), () -> "proyasnil")
                    .create();
            var request = post("/isMember")
                    .header("vk_service_token", vkToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsString(req))
                    .with(token);
            var result = mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andReturn();
            var body = result.getResponse().getContentAsString();
            assertThat(body).contains("Bykov");
        }
    }

//нужен еще тест на реальных данных
}
