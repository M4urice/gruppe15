import game.Debug;

import java.net.SocketException;

import junit.framework.Assert;
import network.Discover;
import network.DiscoverServer;

import org.junit.Test;

public class NetworkTest {

	@Test
	public void test() {
		Debug.setMode(Debug.DEBUG);
		Thread t1 = null, t2 = null;
		try {
			DiscoverServer server = new DiscoverServer("Mein Test Server");
			t1 = new Thread(server);
			t1.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		Discover discover = null;
		discover = new Discover();
		t2 = new Thread(discover);
		t2.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(1, discover.servers.size());
		t1.stop();
		t2.stop();
	}
}
