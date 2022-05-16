package com.techelevator.dao;

import com.techelevator.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcSiteDao implements SiteDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcSiteDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Site> getSitesThatAllowRVs(int parkId) {
        List<Site> siteWithRV = new ArrayList<>();
        String sql = "SELECT s.site_id, s.campground_id, s.site_number, s.max_occupancy, s.accessible, s.max_rv_length, s.utilities " +
                "FROM site s " +
                "JOIN campground c ON c.campground_id = s.campground_id " +
                "JOIN park p ON p.park_id = c.park_id " +
                "WHERE s.max_rv_length > 0 AND p.park_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while (results.next()) {
            siteWithRV.add(mapRowToSite(results));
        }
        return  siteWithRV;
    }

//    public List<Site> getSitesThatAllowRVs(int parkId) {
//        List<Site> siteWithRV = new ArrayList<>();
//        String sql = "SELECT site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
//                "FROM site " +
//                "JOIN campground USING (campground_id) " +
//                "JOIN park USING (park_id) " +
//                "WHERE max_rv_length > 0 AND park_id = ?;";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
//        while (results.next()) {
//            siteWithRV.add(mapRowToSite(results));
//        }
//        return siteWithRV;
//    }

    private Site mapRowToSite(SqlRowSet results) {
        Site site = new Site();
        site.setSiteId(results.getInt("site_id"));
        site.setCampgroundId(results.getInt("campground_id"));
        site.setSiteNumber(results.getInt("site_number"));
        site.setMaxOccupancy(results.getInt("max_occupancy"));
        site.setAccessible(results.getBoolean("accessible"));
        site.setMaxRvLength(results.getInt("max_rv_length"));
        site.setUtilities(results.getBoolean("utilities"));
        return site;
    }

    public List<Site> getAvailableSites( int parkId) {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, \n" +
                "max_rv_length, utilities\n" +
                "FROM site\n" +
                "LEFT JOIN reservation USING (site_id) \n" +
                "JOIN campground USING (campground_id)\n" +
                "RIGHT JOIN park USING (park_id)\n" +
                "WHERE park_id = ? AND reservation_id IS NULL;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while (results.next()) {
            sites.add(mapRowToSite(results));
        }
        return sites;
    }

    @Override
    public List<Site> getAvailableSitesDateRange( int parkId, LocalDate fromDate, LocalDate toDate) {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, \n" +
                "max_rv_length, utilities\n" +
                "FROM site\n" +
                "LEFT JOIN reservation USING (site_id) \n" +
                "JOIN campground USING (campground_id)\n" +
                "RIGHT JOIN park USING (park_id)\n" +
                "WHERE park_id = ? AND reservation_id IS NULL\n" +
                "AND from_date = ? AND to_date = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId, fromDate, toDate);
        while (results.next()) {
            sites.add(mapRowToSite(results));
        }
        return sites;
    }
}
