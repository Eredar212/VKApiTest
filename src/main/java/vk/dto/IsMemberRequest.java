package vk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class IsMemberRequest {
    @JsonProperty("user_id")
    @Positive
    @NotNull
    @Min(1)
    @Schema(description = "Идентификатор пользователя VK", example = "78385")
    private int userId;

    @JsonProperty("group_id")
    @NotBlank
    @Schema(description = "Идентификатор группы VK", example = "93559769")
    private String groupId;

    public int getUserId() {
        return userId;
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public String toString() {
        return "IsMemberRequest{" +
                "userId=" + userId +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
