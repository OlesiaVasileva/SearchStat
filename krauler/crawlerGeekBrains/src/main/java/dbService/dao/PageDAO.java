package dbService.dao;

import dbService.dataSets.Page;
import dbService.dataSets.Site;
import kraulerService.parsingService.LogWork;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PageDAO {

    private Session session;

    public PageDAO(Session session) {
        this.session = session;
    };

    public int insertPage(String url, Site site, Date foundDateTime, Date lastScanDate, String type_page) {
        Page page;
        String long_url;

        int n;

        if (url.length()>50) n =50; else n = url.length();

        if (url.length()>400) {
            long_url = url;
            page = getPageByLongUrl(url);
            url = url.substring(0,399);
        }
        else {
            long_url = "";
            page = getPageByUrl(url);
        }

        if (page == null) {
            if (type_page.equals("link"))
                LogWork.logWrite("            t1:"+url.substring(0, n)+" - INSERT LINK",3);
            else
                LogWork.logWrite("t0:"+url.substring(0, n)+" - INSERT LINK",3);
            return (Integer) session.save(new Page(url, site, foundDateTime, lastScanDate, type_page, long_url));
        }
        return page.getId();
    }

    public void updatePageDate(Page page) {
        page.setLastScanDate(new Date());
        session.update(page);
    }

    public void updatePageDateAndType(Page page, String type) {
        page.setLastScanDate(new Date());
        page.setType_page(type);
        session.update(page);
    }


    public Page getPageById(int id) {
        Page page = (Page) session.get(Page.class, id);
        return page;
    }

    public Page getPageByUrl(String url) {
        Criteria criteria = session.createCriteria(Page.class);

        Object Object = criteria.add(Restrictions.eq("url", url)).uniqueResult();

        return (Page) Object;
    }

    public Page getPageByLongUrl(String url) {
        Criteria criteria = session.createCriteria(Page.class);

        Object Object = criteria.add(Restrictions.eq("long_url", url)).uniqueResult();

        return (Page) Object;
    }

    public List<Page> getNonScannedPages() {

        SQLQuery query = session.createSQLQuery("SELECT * FROM pages WHERE last_scan_date is null");
        query.addEntity(Page.class);
        return query.list();
    }

    public void resetOldSiteMap(Date currentDate, String type) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

        String txtSQL = "UPDATE pages set last_scan_date = null where last_scan_date<>'"+sdfDate.format(currentDate)+"'";
        if (type != "all") {
            txtSQL+= " AND type_page = '"+type+"'";
        }
        SQLQuery query = session.createSQLQuery(txtSQL);

        query.executeUpdate();
    }

    public void resetPageDate(Page page) {

        String txtSQL = "UPDATE pages set last_scan_date = null where id = "+page.getId();
        SQLQuery query = session.createSQLQuery(txtSQL);
        query.executeUpdate();
    }


    public List<Page> getRecalcLinks() {

        String txtSQL = "select * from pages where id in (" +
                "select y.id from persons x, pages y where y.type_page = 'link' and (x.id, y.id) not in ("+
                "select person_id, page_id from person_page_rank)) LIMIT 100";

        SQLQuery query = session.createSQLQuery(txtSQL);

        query.addEntity(Page.class);
        return query.list();
    }


}
