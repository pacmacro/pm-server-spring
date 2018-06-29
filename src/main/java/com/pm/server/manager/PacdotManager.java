package com.pm.server.manager;

import org.springframework.stereotype.Service;

public interface PacdotManager {

    /**
     * Resets the statuses of all pacdots to uneaten.
     *
     */
    void resetPacdots();

}
