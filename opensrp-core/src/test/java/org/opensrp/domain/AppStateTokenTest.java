package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppStateTokenTest {

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(AppStateToken.class));
    }

    @Test
    public void testEqualAndHashcodeContract() {
        EqualsVerifier.forClass(AppStateToken.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .verify();
    }

    @Test
    public void testValueBasedOnType() {
        AppStateToken appStateToken = new AppStateToken("name", "2.3", 3l);

        assertEquals(2.3, appStateToken.doubleValue(), 0);
        assertEquals(2.3, appStateToken.floatValue(), 0.2);
        assertEquals("2.3", appStateToken.stringValue());


        appStateToken = new AppStateToken("name", "true", 3l);
        assertTrue(appStateToken.booleanValue());

        appStateToken = new AppStateToken("name", "2", 3l);
        assertEquals(2, appStateToken.intValue());

        appStateToken = new AppStateToken("name", new DateTime(0l).toLocalDate(), 3l);
        assertEquals(new DateTime(0l).toLocalDate(), appStateToken.datetimeValue());
    }
}
