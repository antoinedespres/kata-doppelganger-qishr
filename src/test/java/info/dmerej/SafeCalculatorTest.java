package info.dmerej;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DummyAuthorizer implements Authorizer {
  @Override
  public boolean authorize() {
    return true;
  }
}


public class SafeCalculatorTest {
  
  @Test
  void should_not_throw_when_authorized() {
    Authorizer authorizer = new DummyAuthorizer();
    SafeCalculator calculator = new SafeCalculator(authorizer);

    assertEquals(3, calculator.add(1, 2));
  }

  @Test
  void should_not_throw_when_authorized_mockito() {
    Authorizer authorizer = mock();
    when(authorizer.authorize()).thenReturn(true);

    SafeCalculator calculator = new SafeCalculator(authorizer);

    assertEquals(3, calculator.add(1, 2));
  }
}
