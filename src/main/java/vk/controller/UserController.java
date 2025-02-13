package vk.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.json.JSONObject;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vk.dto.IsMemberRequest;
import vk.utils.GetAPIParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
public class UserController {
    @Tag(name = "Получение информации о пользователе", description = "Получение ФИО и наличие пользователя в группе")
    @Cacheable(value = "member", key = "#isMemberRequest?.userId + '-' + #isMemberRequest?.groupId")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/isMember")
    public String getUserMembershipInfo(@RequestHeader(value = "vk_service_token", required = false)
                                        @Parameter(description = "Токен VK")
                                        @NotBlank(message = "vk_service_token не должен быть пустым")
                                            String token,
                                        @RequestBody(required = false)
                                        @Validated
                                        @Parameter(description = "Запрос в формате JSON")
                                        IsMemberRequest isMemberRequest) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (isMemberRequest == null) {
                return mapper.writeValueAsString(Map.of("error", "Не передано тело запроса"));
            }
            if (token == null) {
                return mapper.writeValueAsString(Map.of("error", "Не передан токен"));
            }
            System.out.println("getting membership info" + isMemberRequest.toString());

            int userId = isMemberRequest.getUserId();
            String groupId = isMemberRequest.getGroupId();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.add("Content-Type", "application/x-www-form-encoded");

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity requestIsMember = new HttpEntity<>(headers);
            HttpEntity requestUserGet = new HttpEntity<>(headers);


            UriComponentsBuilder builderIsMember = UriComponentsBuilder.fromHttpUrl(GetAPIParams.groupsIsMember());
            builderIsMember.queryParam("user_id", userId);
            builderIsMember.queryParam("group_id", groupId);
            builderIsMember.queryParam("v", GetAPIParams.getVersion());
            ResponseEntity<String> responseIsMember = restTemplate.exchange(builderIsMember.build().encode().toUri(),
                    HttpMethod.POST, requestIsMember, String.class);

            UriComponentsBuilder builderUserGet = UriComponentsBuilder.fromHttpUrl(GetAPIParams.usersGet());
            builderUserGet.queryParam("user_ids", userId);
            builderUserGet.queryParam("fields", "first_name", "last_name", "nickname");
            builderUserGet.queryParam("v", GetAPIParams.getVersion());
            ResponseEntity<String> responseUserGet = restTemplate.exchange(builderUserGet.build().encode().toUri(),
                    HttpMethod.POST, requestUserGet, String.class);

            /*boolean member;
            String userFName;
            String userLName;
            String userMName;*/
            JSONObject result = new JSONObject();
            Map<String, Object> responseMap = mapper.readValue(responseUserGet.getBody(), Map.class);
            System.out.println("getting USER info" + responseMap.toString());
            if (responseMap.containsKey("error")) {
                return mapper.writeValueAsString(Map.of("error", ((HashMap<String,String>)(responseMap.get("error"))).get("error_msg")));
            } else if (responseMap.containsKey("response")) {
                var response = ((ArrayList<HashMap<String,String>>)(responseMap.get("response"))).get(0);
                result.put("last_name", response.get("last_name"));
                result.put("first_name", response.get("first_name"));
                result.put("middle_name", response.get("nickname"));
            } else {
                return mapper.writeValueAsString(Map.of("error",
                        "Неизвестная ошибка получения информации о пользователе"));
            }
            responseMap = mapper.readValue(responseIsMember.getBody(), Map.class);
            System.out.println("getting IsMEMBER info" + responseMap.toString());
            if (responseMap.containsKey("error")) {
                return mapper.writeValueAsString(Map.of("error", ((HashMap<String,String>)(responseMap.get("error"))).get("error_msg")));
            } else if (responseMap.containsKey("response")) {
                result.put("member", responseMap.get("response").toString().equals("1"));
            } else {
                return mapper.writeValueAsString(Map.of("error",
                        "Неизвестная ошибка получения данных об участии в группе"));
            }
            //System.out.println("get membership info" + result.toString());
            return result.toString();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

