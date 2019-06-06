package com.pm.server.manager;

import com.pm.server.TestTemplate;
import com.pm.server.datatype.Pacdot;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.registry.PlayerRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminManagerTest extends TestTemplate {

    private List<Pacdot> pacdotList = new ArrayList<>();

    @Mock
    private PacdotRegistry mockPacdotRegistry;

    @Mock
    private PlayerRegistry mockPlayerRegistry;

    private AdminManager adminManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(mockPacdotRegistry.getInformationOfAllPacdots())
                .thenReturn(pacdotList);

        adminManager = new AdminManagerImpl(
                mockPacdotRegistry, mockPlayerRegistry
        );
    }

    @Test
    public void unitTest_resetPacdots() {

        // Given
        for(int i = 0; i < 10; i++) {
            pacdotList.add(new Pacdot());
        }
        for(Pacdot p : pacdotList) {
            p.setEaten();
        }

        // When
        adminManager.resetPacdots();

        // Then
        verify(mockPacdotRegistry).resetPacdots();

    }

    @Test
    public void unitTest_resetPacdots_noPacdots() {

        // Given
        pacdotList.clear();

        // When
        adminManager.resetPacdots();

        // Then
        // No exception

    }

}
