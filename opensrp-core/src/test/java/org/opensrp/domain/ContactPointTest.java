package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContactPointTest {

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(ContactPoint.class));
    }

    @Test
    public void testConstructor() {
        final Class<?> clazz = ContactPoint.class;
        final Object obj1 = FormTest.getInstance(clazz,
                "type","use", "number",0, new DateTime(0l), new DateTime(1l));
        Affirm.affirmNotNull("Should have created an object", obj1);
        ContactPoint contactPoint = (ContactPoint) obj1;
        assertEquals("type", contactPoint.getType());
        assertEquals("use", contactPoint.getUse());
        assertEquals("number", contactPoint.getNumber());
        assertEquals(0, contactPoint.getPreference());
        assertEquals(new DateTime(0l), contactPoint.getStartDate());
        assertEquals(new DateTime(1l), contactPoint.getEndDate());

        final Object obj2 = FormTest.getInstance(clazz, new Object[] {});
        Affirm.affirmTrue("Should have created a different object", obj1 != obj2);
    }
}
