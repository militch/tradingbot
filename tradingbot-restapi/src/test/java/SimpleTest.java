import com.github.militch.tradingbot.restapi.exchange.BinanceClient;
import com.github.militch.tradingbot.restapi.exchange.SteamHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class SimpleTest implements SteamHandler {
    private BinanceClient client = new BinanceClient();
    @Test
    public void test() {
        client.openStream(new String[]{""}, this);
    }

    @Override
    public void onTrade() {

    }

    @Override
    public void onConnect() {

    }
}
