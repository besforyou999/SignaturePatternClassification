package jhs.signserver.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClientService {

    public String sendImage(String encoded_str) {
        try (Socket client = new Socket()) {
            InetSocketAddress ipep = new InetSocketAddress("127.0.0.1", 9999);

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
                //System.out.println(encoded_str);
                return encoded_str;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
