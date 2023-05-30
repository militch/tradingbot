import com.github.militch.tradingbot.restapi.BinanceSteamHandlerImpl;
import com.github.militch.tradingbot.restapi.exchange.BinanceClient;
import com.github.militch.tradingbot.restapi.exchange.SteamHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class SimpleTest {
    private final BinanceClient client = new BinanceClient();
    private final SteamHandler handler = new BinanceSteamHandlerImpl();
    @Test
    public void test() throws Exception {
        CountDownLatch cdl = new CountDownLatch(1);
        client.setSteamHandler(handler);
        try{
            client.subscribe("btcusdt@trade");
        }catch (Exception e) {
            log.error("error1", e);
        }
        cdl.await();
    }

}
