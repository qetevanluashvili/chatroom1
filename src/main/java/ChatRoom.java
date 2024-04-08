import jakarta.persistence.*;

@Entity
@Table(name = "chatrooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private int chatRoomId;

    @Column(name = "chatroom_name", nullable = false, unique = true)
    private String chatRoomName;

    @Column(name = "member_count", columnDefinition = "INT DEFAULT 0")
    private int memberCount;

   public ChatRoom(String chatroomName) {
        this.chatRoomName = chatroomName;
    }


    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}

