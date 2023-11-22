package info.dmerej;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

record Notification(User user, String message) {}

class SpyNotifier implements Notifier{
  private final List<Notification> notificationList;
  @Override
  public void notify(User user, String message) {
    notificationList.add(new Notification(user, message));
  }

  public SpyNotifier() {
    notificationList = new ArrayList<>();
  }

  public ArrayList<Notification> getNotifications() {
    return new ArrayList<>(notificationList);
  }
}

public class DiscountApplierTest {
  @Test
  void should_notify_twice_when_applying_discount_for_two_users_v1() {
    SpyNotifier notifier = new SpyNotifier();
    DiscountApplier discountApplier = new DiscountApplier(notifier);
    User user1 = new User("michel", "michel@efrei.net");
    User user2 = new User("bastien", "bastien@efrei.net");
    List<User> users = List.of(user1, user2);

    discountApplier.applyV1(10, users);

    assertEquals(2, notifier.getNotifications().size());
  }

  @Test
  void should_notify_twice_when_applying_discount_for_two_users_v2() {
    SpyNotifier notifier = new SpyNotifier();
    DiscountApplier discountApplier = new DiscountApplier(notifier);
    User user1 = new User("michel", "michel@efrei.net");
    User user2 = new User("bastien", "bastien@efrei.net");
    List<User> users = List.of(user1, user2);

    discountApplier.applyV2(10, users);

    assertEquals(users, notifier.getNotifications().stream().map(Notification::user).toList());
  }

  @Test
  void should_notify_twice_when_applying_discount_for_two_users_v1_mockito() {
    Notifier notifier = Mockito.mock(Notifier.class);
    DiscountApplier discountApplier = new DiscountApplier(notifier);
    User user1 = new User("michel", "michel@efrei.net");
    User user2 = new User("bastien", "bastien@efrei.net");

    discountApplier.applyV1(10, List.of(user1, user2));

    Mockito.verify(notifier, Mockito.times(2)).notify(Mockito.any(), Mockito.any());
  }

  @Test
  void should_notify_twice_when_applying_discount_for_two_users_v2_mockito() {

  }

}
