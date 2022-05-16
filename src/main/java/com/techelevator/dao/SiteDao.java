package com.techelevator.dao;

import com.techelevator.model.Site;

import java.time.LocalDate;
import java.util.List;

public interface SiteDao {

    List<Site> getSitesThatAllowRVs(int parkId);

    List<Site> getAvailableSites_Should_ReturnSites( int parkId );
//    List<Site> getAvailableSites(int siteId);
}
