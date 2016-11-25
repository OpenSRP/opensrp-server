package org.opensrp.repository.it;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.opensrp.dto.AlertStatus.normal;
import static org.opensrp.dto.BeneficiaryType.mother;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.domain.Drug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.opensrp.repository.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp.xml")
public class AllOrderTest {
    @Autowired
    AllDrugs allDrugs;

    @Before
    public void setUp() throws Exception {
       allDrugs.removeAll();
    }
    
    @Test
    public void shouldSaveADrug() throws Exception {
    	Drug Drugs = new Drug("525d1ccc-2f5c-49ec-b5f6-eea2206c0c5d");
        allDrugs.add(Drugs);

        List<Drug> allTheDrugInDB = allDrugs.getAll();
        assertEquals(1, allTheDrugInDB.size());
        assertEquals(Drugs, allTheDrugInDB.get(0));
    }

}
