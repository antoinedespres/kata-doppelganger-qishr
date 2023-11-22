package info.dmerej;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    // TODO: write a test to demonstrate the bug in DiscountApplier.applyV2()
  }

}
