package Server;

import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import javax.imageio.*;


public class Client {
    Socket socket;
    Formatter formatter;
    String username;
    private ClientHandler clientHandler;
    public boolean loggedIn = false;
    public static Gson gson = new Gson();

    public ClientHandler getClientHandler(){ return clientHandler;}
    public void send(Message message) {
        String json = gson.toJson(message) + "#";
        formatter.format(json);
        formatter.flush();
    }

    public Message sendAndGetResult(Message message) throws Exception {
        String json = gson.toJson(message) + "#";
        formatter.format(json);
        formatter.flush();
        while (true) {
            if (clientHandler.results.containsKey(message.message_id)) {
                return clientHandler.results.get(message.message_id);
            } else {
                Thread.sleep(1);
            }
//            System.err.println("stuck in while");

        }
    }


    public Message send_username() throws Exception {
        return this.sendAndGetResult(Message.loginCommand(this.username));
    }

    public void connect() throws Exception {
        socket = new Socket("localhost", 3456);
        formatter = new Formatter(socket.getOutputStream());
        clientHandler = new ClientHandler(this);
        ClientReader clientReader = new ClientReader(socket, clientHandler);
        this.pinging();
    }

    public boolean login(String username) throws Exception {
        this.username = username;
        Message message = send_username();

        if (message.command == Command.INVALID_USERNAME) {
            System.out.println(message.data);
            return false;
        }
        loggedIn = true;
        return true;
    }


    public void showUsers() {
        Message message = Message.showUsers();
        this.send(message);
    }

    public void pinging() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                if(loggedIn)
                    this.send(Message.ping(System.currentTimeMillis()));
            }

        }).start();
    }

    public Message gameRequest(String to) throws Exception{
        Message message = Message.gameRequest(this.username, to);
        return sendAndGetResult(message);
    }

    public Message accept(String to) throws Exception{
        Message message = Message.accept(this.username, to);
        return sendAndGetResult(message);
    }

    public void sendChat(String to, String chat) {
        Message message = Message.chat(this.username, to, chat);
        send(message);
    }

    public void sendPic(String to, String jpgPath) throws Exception {
        ////     fghat jpg mishe ferestad!
        BufferedImage bufferimage = ImageIO.read(new File(jpgPath));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufferimage, "jpg", output);
        byte[] data = output.toByteArray();
        String base64 = Base64.encode(data);
//        System.out.println(base64);
        Message message = Message.chatPic(this.username, to, base64);
        send(message);


    }

    public void reply(String to, String chat, String replyTo) {
        Message message = Message.chat(this.username, to, chat, replyTo);
        send(message);
    }

    public void scoreBoard(){
        Message message = Message.scoreBoardClient();
        send(message);
    }

    public String getUsername(){return username;}



}
