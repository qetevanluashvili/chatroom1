import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.Message;
import lombok.Data;

import java.util.List;

@Data
public class GetChatRoomsResponse {
    @JsonProperty("chatRooms")
    private List<ChatRoom> chatRooms;




}
