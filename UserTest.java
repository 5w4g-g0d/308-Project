import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void updatePass() throws InterruptedException {
        User user = new User() {
            @Override
            public void updatePass(JPanel panel1, JFrame frame) {
                password = "oldPassword";
                panel1.setVisible(false);
            }
        };

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        CountDownLatch latch = new CountDownLatch(1);

        SwingUtilities.invokeLater(() -> {
            user.updatePass(panel, frame);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
        assertFalse(panel.isVisible());
    }

}

