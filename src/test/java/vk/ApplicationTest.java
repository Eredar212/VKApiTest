package vk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vk.dto.IsMemberRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;

    @Test
    public void testNullBody() throws Exception {
        var request = get("/")
                .header("vk_service_token", "123123124")
                .contentType(MediaType.APPLICATION_JSON);
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
        var request = get("/")
                .contentType(MediaType.APPLICATION_JSON);
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
                .supply(Select.field(IsMemberRequest::getGroupId), () -> "121123")
                .create();
        var request = get("/")
                .header("vk_service_token", "123123124")
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(req));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("error");
    }

//нужен еще тест на реальных данных
}
