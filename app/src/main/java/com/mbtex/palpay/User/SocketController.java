package com.mbtex.palpay.User;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketController {

    static final String SOCKET_URL = "StaticUlr";
    private Socket mSocket;

    private SocketController() throws URISyntaxException {
        mSocket = IO.socket(this.SOCKET_URL);
    }
}
