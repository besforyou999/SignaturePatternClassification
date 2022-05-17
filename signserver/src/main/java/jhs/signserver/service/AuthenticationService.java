package jhs.signserver.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Service
@Transactional
public class AuthenticationService {

    public void registerSign(String encoded_str) {
        try (Socket client = new Socket()) {
            InetSocketAddress ipep = new InetSocketAddress("", 9990);

            client.connect(ipep);

            try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {

                byte[] data = encoded_str.getBytes();
                ByteBuffer b = ByteBuffer.allocate(4);
                b.order(ByteOrder.LITTLE_ENDIAN);
                b.putInt(data.length);

                sender.write(b.array(), 0, 4);
                sender.write(data);

                data = new byte[4];

                receiver.read(data, 0, 4);

                //ByteBuffer
                b = ByteBuffer.wrap(data);
                b.order(ByteOrder.LITTLE_ENDIAN);
                int length = b.getInt();

                data = new byte[length];

               receiver.read(data, 0, length);

                encoded_str = new String(data, "UTF-8");


            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
