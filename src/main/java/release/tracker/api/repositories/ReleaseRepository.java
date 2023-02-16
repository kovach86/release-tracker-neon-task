package release.tracker.api.repositories;

import release.tracker.api.model.domain.DbHibernateManager;
import release.tracker.api.model.domain.Release;
import release.tracker.api.repositories.interfaces.IReleaseRepository;
import release.tracker.api.utilities.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReleaseRepository implements IReleaseRepository {


    private Session sessionFactory = DbHibernateManager.getSessionFactory().openSession();

    public List<Release> retrieve() {
        return sessionFactory.createQuery("from Release", Release.class).list();
    }

    public Release getById(int releaseId) {
        return sessionFactory.get(Release.class, releaseId);
    }

    public Release insert(Release releaseTable) {
        Transaction tx = sessionFactory.beginTransaction();
        try {
            sessionFactory.persist(releaseTable);
            tx.commit();
        } catch (HibernateException exc) {
            tx.rollback();
        }
        return releaseTable;
    }

    public int update(Release releaseTable) {
        int rowsUpdated = 0;
        Transaction tx = sessionFactory.beginTransaction();
        try {
            sessionFactory.merge(releaseTable);
            tx.commit();
        } catch (HibernateException exc) {
            tx.rollback();
        }
        return rowsUpdated;
    }

    public void delete(int releaseId) {
        Transaction tx = sessionFactory.beginTransaction();
        try {
            String hql = "Delete Release Where id= :releaseId";
            Query query = sessionFactory.createQuery(hql);
            query.setParameter("releaseId", releaseId);
            query.executeUpdate();

            tx.commit();
        } catch (HibernateException exc) {
            tx.rollback();
        }
    }

    public Map<Integer, List<Release>> filter(String releaseDate, String lastUpdateDate, String createdDate, String releaseStatus, String description, String name,
                                              int page, int recordsToTake) {

        String countSql = "SELECT Count(id) FROM release_tbl Where\n";
        String selectSql = "SELECT * FROM release_tbl Where\n";
        var result = new HashMap<Integer, List<Release>>();

        StringBuilder filteredSql = new StringBuilder().append("release_date").append(">=").append("'").append(releaseDate).append("'\n");
        int skipRecords = page == 0 ? 0 : recordsToTake * (page - 1);
        if (!StringUtils.isStringNullOrEmpty(lastUpdateDate))
            filteredSql.append("AND ").append("last_update_ad>=").append("'").append(lastUpdateDate).append("'\n");
        if (!StringUtils.isStringNullOrEmpty(createdDate))
            filteredSql.append("AND ").append("created_at>=").append("'").append(createdDate).append("'\n");
        if (!StringUtils.isStringNullOrEmpty(releaseStatus))
            filteredSql.append("AND ").append("status").append(" LIKE ").append("'%").append(releaseStatus).append("%'\n");
        if (!StringUtils.isStringNullOrEmpty(description))
            filteredSql.append("AND ").append("description").append(" LIKE ").append("'%").append(description).append("%'\n");
        if (!StringUtils.isStringNullOrEmpty(name))
            filteredSql.append("AND ").append("name").append(" LIKE ").append("'%").append(name).append("%'\n");

        int countFilteredRecords = sessionFactory.createNativeQuery(countSql + filteredSql + ";", Integer.class).getSingleResult();
        if (countFilteredRecords == 0)
            return new HashMap<>();

        filteredSql.append("LIMIT ").append(recordsToTake).append("\n OFFSET ").append(skipRecords);
        List<Release> releaseList = sessionFactory.createNativeQuery(selectSql + filteredSql, Release.class).list();
        result.put(countFilteredRecords, releaseList);
        return result;
    }
}
