package com.olesix.searchstat.model.impl;

import java.util.List;

import com.olesix.searchstat.model.IGeneralStatApi;
import com.olesix.searchstat.model.entity.GenStatDataItem;
import com.olesix.searchstat.model.entity.Site;
import rx.Observable;

/**
 * Created by Olesia on 21.04.2018.
 */

public class GeneralStatApi implements IGeneralStatApi {

    private RestApi restApi = RestApi.getInstance();

    @Override
    public Observable<List<Site>> getSites(String token) {

        return restApi.getRestApi().getSites(token);
    }

    @Override
    public Observable<List<GenStatDataItem>> getGeneralStat(String token, String site) {

        return restApi.getRestApi().getGeneralStatistic(token, site);
    }
}
