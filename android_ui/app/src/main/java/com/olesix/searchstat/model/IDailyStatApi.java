package com.olesix.searchstat.model;

import java.util.List;

import com.olesix.searchstat.model.entity.DailyStatisticsModel;
import com.olesix.searchstat.model.entity.Person;
import com.olesix.searchstat.model.entity.Site;
import rx.Observable;

/**
 * Created by Olesia on 16.05.2018.
 */

public interface IDailyStatApi {
    Observable<List<Site>> getSites(String token);
    Observable<List<Person>> getPersons(String token);
    Observable<List<DailyStatisticsModel>> getDailyStat(String token, String person, String date1, String date2, String site);
}
