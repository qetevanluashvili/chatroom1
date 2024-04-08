import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.List;


public class Service {
    private static final Service instance = new Service();
    private final EntityManagerFactory entityManagerFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static Service getInstance() {
        return instance;
    }

    private Service() {
        entityManagerFactory = Persistence.createEntityManagerFactory("ChatRoomPersistenceUnit");
    }

    public CreateChatRoomRequest createChatRoom(CreateChatRoomRequest request) {
        return executeTransaction(entityManager -> {
           CreateChatRoomRequest response;
            response = new CreateChatRoomRequest();
            try {
                entityManager.persist(new ChatRoom(request.getChatroomName()));
                response.setSuccess(true);
                response.setMessage("Chat room created successfully");
            } catch (Exception e) {
                response.setSuccess(false);
                response.setMessage("Failed to create chat room");
                e.printStackTrace();
            }
            return response;
        });
    }

    public GetChatRoomsResponse getChatRooms() {
        return executeTransaction(entityManager -> {
            GetChatRoomsResponse response = new GetChatRoomsResponse();
            try {
                CriteriaQuery<ChatRoom> criteriaQuery = createCriteriaQuery(ChatRoom.class);
                List<ChatRoom> chatRooms = entityManager.createQuery(criteriaQuery).getResultList();
                response.setChatRooms(chatRooms);
            } catch (Exception e) {
                response.setChatRooms(null);
                e.printStackTrace();
            }
            return response;
        });
    }

    private <T> T executeTransaction(TransactionFunction<T> transactionFunction) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T result = transactionFunction.apply(entityManager);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to execute transaction", e);
        } finally {
            entityManager.close();
        }
    }


    private <T> CriteriaQuery<T> createCriteriaQuery(Class<T> resultClass) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.getCriteriaBuilder().createQuery(resultClass);
        } finally {
            entityManager.close();
        }
    }


    @FunctionalInterface
    private interface TransactionFunction<T> {
        T apply(EntityManager entityManager);
    }
}
