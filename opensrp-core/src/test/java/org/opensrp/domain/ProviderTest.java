package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProviderTest {

    @Test
    public void testEqualAndHashcodeContract() {
        EqualsVerifier.forClass(Provider.class)
                .withIgnoredFields("id", "revision")
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(User.class, new User("ll"), new User("e"))
                .withRedefinedSuperclass()
                .verify();
    }

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(Provider.class));
    }

    @Test
    public void testConstructor() {
        final Class<?> clazz = Provider.class;
        final Object obj1 = FormTest.getInstance(clazz, "entityId", "name");
        Affirm.affirmNotNull("Should have created an object", obj1);

        Provider provider = (Provider) obj1;
        assertEquals("entityId", provider.getBaseEntityId());
        assertEquals("name", provider.getFullName());

        final Object obj2 = FormTest.getInstance(clazz, "entityId");
        provider = (Provider) obj2;
        assertEquals("entityId", provider.getBaseEntityId());
        assertEquals(null, provider.getFullName());
        Affirm.affirmTrue("Should have created a different object", obj1 != obj2);
    }
}
