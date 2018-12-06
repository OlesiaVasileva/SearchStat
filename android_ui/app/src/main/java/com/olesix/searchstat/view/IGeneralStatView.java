package com.olesix.searchstat.view;

import java.util.List;

import com.olesix.searchstat.model.entity.GenStatDataItem;
import com.olesix.searchstat.model.entity.Site;

/**
 * Created by Olesia on 21.04.2018.
 */

public interface IGeneralStatView {
    void hideLoadingIndicator();
    void showNoConnectionMessage();
    void showNoConnectionToTheServer();
    void updateSites(List<Site> sites);
    void updateStat(List<GenStatDataItem> listGS);
    void setStat(List<GenStatDataItem> listGS);
}
