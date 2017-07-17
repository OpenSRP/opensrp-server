package org.opensrp.domain;


import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.construct.InstanceFactory;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Test;

import static java.util.Arrays.asList;

public class FormTest {

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(Form.class));
    }

    @Test
    public void testConstructor() {
        final Class<?> clazz = Form.class;
        final Object obj1 = getInstance(clazz, "bind","bind_path", asList(new FormField()), asList(new SubFormDefinition()));
        Affirm.affirmNotNull("Should have created an object", obj1);

        final Object obj2 = getInstance(clazz, new Object[] {});
        Affirm.affirmTrue("Should have created a different object", obj1 != obj2);
    }

    public static Object getInstance(final Class<?> clazz, final Object... parameters) {
        final PojoClass pojoClass = PojoClassFactory.getPojoClass(clazz);
        return InstanceFactory.getInstance(pojoClass, parameters);
    }
}
