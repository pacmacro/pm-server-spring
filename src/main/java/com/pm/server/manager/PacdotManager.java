package com.pm.server.manager;

import com.pm.server.datatype.Pacdot;
import org.springframework.stereotype.Service;

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

}
