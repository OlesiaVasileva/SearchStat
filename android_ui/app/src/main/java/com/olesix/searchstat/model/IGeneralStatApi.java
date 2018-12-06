package com.olesix.searchstat.model;

import java.util.List;
import com.olesix.searchstat.model.entity.GenStatDataItem;
import com.olesix.searchstat.model.entity.Site;
import rx.Observable;

/**
 * Created by Olesia on 21.04.2018.
 */

public interface IGeneralStatApi {
    Observable<List<Site>> getSites(String token);
    Observable<List<GenStatDataItem>> getGeneralStat(String token, String site);
}
