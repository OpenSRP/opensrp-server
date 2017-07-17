package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class SubFormDefinitionTest {

    @Test
    @SuppressWarnings("RedundantArrayCreation")
    public void shouldCreateUsingDefaultConstructor() {
        final Class<?> clazz = SubFormDefinition.class;
        final Object obj1 = FormTest.getInstance(clazz, (Object[]) null);
        SubFormDefinition subFormDefinition = (SubFormDefinition) obj1;
        assertEquals("", subFormDefinition.name());
        Affirm.affirmNotNull("Should have created an object", obj1);

        final Object obj2 = FormTest.getInstance(clazz, new Object[] {});
        subFormDefinition = (SubFormDefinition) obj2;
        assertEquals("", subFormDefinition.name());
        Affirm.affirmTrue("Should have created a different object", obj1 != obj2);
    }

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(SubFormDefinition.class));
    }

    @Test
    public void testConstructorWithParameter() {
        final Class<?> clazz = SubFormDefinition.class;
        final Object obj1 = FormTest.getInstance(clazz, "bind", Collections.EMPTY_LIST);
        SubFormDefinition subFormDefinition = (SubFormDefinition) obj1;
        Affirm.affirmNotNull("Should have created an object", obj1);
        assertEquals("bind", subFormDefinition.name());
        assertEquals(Collections.EMPTY_LIST, subFormDefinition.getFields());

        final Object obj2 = FormTest.getInstance(clazz, new Object[] {});
        Affirm.affirmTrue("Should have created a different object", obj1 != obj2);
    }
}
