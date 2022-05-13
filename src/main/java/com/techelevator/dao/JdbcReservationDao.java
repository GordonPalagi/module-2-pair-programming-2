package com.techelevator.dao;

import com.techelevator.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;

//import static jdk.vm.ci.meta.JavaKind.Int;

public class JdbcReservationDao implements ReservationDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
        Reservation newReservation = new Reservation();
        String sql = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date)" +
                "VALUES(?,?,?,?,?) RETURNING reservation_id;";
        int newReservation_id = jdbcTemplate.queryForObject(sql, int.class, newReservation.getSiteId(), newReservation.getName(), newReservation.getFromDate(), newReservation.getToDate());
        newReservation.setReservationId(newReservation_id);
                return newReservation_id;
    }

    private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation r = new Reservation();
        r.setReservationId(results.getInt("reservation_id"));
        r.setSiteId(results.getInt("site_id"));
        r.setName(results.getString("name"));
        r.setFromDate(results.getDate("from_date").toLocalDate());
        r.setToDate(results.getDate("to_date").toLocalDate());
        r.setCreateDate(results.getDate("create_date").toLocalDate());
        return r;
    }


}
