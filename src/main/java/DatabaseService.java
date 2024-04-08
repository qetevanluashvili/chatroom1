import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
public class DatabaseService {
    private static final DatabaseService instance = new DatabaseService();
    private final EntityManagerFactory entityManagerFactory;

    DatabaseService() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("ChatRoomPersistenceUnit");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize EntityManagerFactory");
        }
    }

    public static DatabaseService getInstance() {
        return instance;
    }

    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public <T> T executeTransaction(TransactionFunction<T> transactionFunction) {
        EntityManager entityManager = createEntityManager();
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

    public <T> CriteriaQuery<T> createCriteriaQuery(Class<T> resultClass) {
        EntityManager entityManager = createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            return criteriaBuilder.createQuery(resultClass);
        } finally {
            entityManager.close();
        }
    }

    @FunctionalInterface
    public interface TransactionFunction<T> {
        T apply(EntityManager entityManager);
    }
}
