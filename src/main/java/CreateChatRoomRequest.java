import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateChatRoomRequest {
    @JsonProperty("chatroomName")
    private String chatroomName;


    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;
}

