package com.pm.server.manager;

import com.pm.server.datatype.Pacdot;

import java.util.List;

public interface PacdotManager {

    /**
     * Resets the statuses of all pacdots to uneaten.
     *
     */
    void resetPacdots();

    /**
     * Retrieves the information of all pacdots in the registry.
     *
     * @return the information of all pacdots
     */
    List<Pacdot> getInformationOfAllPacdots();

    /**
     * Retrieves the total number of Pacdots, eaten or uneaten.
     *
     * @return the total number of Pacdots, eaten or uneaten
     */
    Integer getTotalCount();

    /**
     * Retrieves the number of uneaten Pacdots.
     *
     * @return the number of uneaten Pacdots
     */
    Integer getUneatenCount();

    /**
     * Retrieves the number of uneaten Powerdots.
     *
     * @return the number of uneaten Powerdots
     */
    Integer getUneatenPowerdotCount();

}
