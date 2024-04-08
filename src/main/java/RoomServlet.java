import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/room")
public class RoomServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<ChatRoom> chatRooms = new ArrayList<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        try {

            String jsonResponse = objectMapper.writeValueAsString(chatRooms);
            response.getWriter().println(jsonResponse);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("{\"error\": \"Failed to retrieve chat rooms\"}");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        try {

            ChatRoom chatRoom = objectMapper.readValue(request.getReader(), ChatRoom.class);

            chatRooms.add(chatRoom);

            String jsonResponse = objectMapper.writeValueAsString(chatRoom);
            response.getWriter().println(jsonResponse);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{\"error\": \"Invalid request body\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("{\"error\": \"Failed to create chat room\"}");
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().println("{\"error\": \"PUT method not supported\"}");
    }
}
