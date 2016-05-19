package hand.framework.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Edward on 16/5/17.
 */
public class WrappedOutputStream extends ServletOutputStream {
    private ByteArrayOutputStream buffer;

    public WrappedOutputStream(ByteArrayOutputStream buffer) {
        this.buffer = buffer;
    }

    public void write(int b) throws IOException {
        buffer.write(b);
    }

    public byte[] toByteArray() {
        return buffer.toByteArray();
    }

    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener writeListener) {

    }
}
