package org.opensrp.domain;


import com.openpojo.random.RandomFactory;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Test;

import javax.management.ReflectionException;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class FormFieldTest {

    @Test
    @SuppressWarnings("RedundantArrayCreation")
    public void shouldCreateUsingDefaultConstructor() {
        final Class<?> clazz = FormField.class;
        final Object obj1 = FormTest.getInstance(clazz, (Object[]) null);
        Affirm.affirmNotNull("Should have created an object", obj1);

        final Object obj2 = FormTest.getInstance(clazz, new Object[] {});
        Affirm.affirmTrue("Should have created a different object", obj1 != obj2);
    }

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(FormField.class));
    }

    @Test
    public void testConstructorWithParameter() {
        final Class<?> clazz = FormField.class;
        final Object obj1 = FormTest.getInstance(clazz, "bind","bind_path");
        FormField formField = (FormField) obj1;
        Affirm.affirmNotNull("Should have created an object", obj1);
        assertEquals("bind", formField.name());
        assertEquals("bind_path", formField.bind());

        final Object obj2 = FormTest.getInstance(clazz, new Object[] {});
        Affirm.affirmTrue("Should have created a different object", obj1 != obj2);
    }

}
