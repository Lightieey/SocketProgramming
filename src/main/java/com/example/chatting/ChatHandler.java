package com.example.chatting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ChatHandler extends Thread{

    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private Socket socket;
    private List<ChatHandler> list;
    private static Set<String> nameList = new LinkedHashSet<>();

    public ObjectInputStream getReader() {
        return reader;
    }

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public ChatHandler(Socket socket, List<ChatHandler> list) throws IOException {
        this.socket = socket;
        this.list = list;
        writer = new ObjectOutputStream(socket.getOutputStream());
        reader = new ObjectInputStream(socket.getInputStream());
    }

    public void run() {
        InfoDTO dto = null;
        String nickName;
        try {
            while (true) {
                dto = (InfoDTO)reader.readObject();
                nickName = dto.getNickName();

                if (dto.getCommand() == Info.EXIT) {
                    InfoDTO sendDto = new InfoDTO();

                    sendDto.setCommand(Info.EXIT);
                    writer.writeObject(sendDto);
                    writer.flush();

                    reader.close();
                    writer.close();
                    socket.close();

                    list.remove(this);
                    nameList.remove(nickName);

                    sendDto.setCommand(Info.SEND);
                    sendDto.setMessage("*** " + nickName + " exited ***");
                    System.out.println("*** ["+nickName+"] exited ***");

                    broadcast(sendDto);
                    break;
                } else if (dto.getCommand() == Info.JOIN) {
                    InfoDTO sendDto = new InfoDTO();
                    sendDto.setCommand(Info.SEND);
                    if ("server".equals(nickName)) {
                        sendDto.setMessage("[***** server mode *****]");
                        System.out.println("[***** server mode *****]");
                    } else {
                        sendDto.setMessage("*** " + nickName + " joined ***");
                        System.out.println("*** ["+nickName+"] joined ***");
                    }

                    nameList.add(nickName);
                    broadcast(sendDto);
                } else if (dto.getCommand() == Info.SEND) {
                    InfoDTO sendDto = new InfoDTO();
                    sendDto.setCommand(Info.SEND);
                    sendDto.setMessage("["+nickName+"] " + dto.getMessage());
                    System.out.println(sendDto.getMessage());
                    broadcast(sendDto);
                } else if (dto.getCommand() == Info.WHISPER) {
                    InfoDTO sendDto = new InfoDTO();
                    sendDto.setCommand(Info.SEND);

                    String msg = dto.getMessage();
                    int firstIdx = msg.indexOf(" ");
                    int SecondIdx = msg.indexOf(" ", 4);

                    String toWhisper = msg.substring(firstIdx + 1, SecondIdx);
                    String whisperMsg = msg.substring(SecondIdx+1);
                    sendDto.setMessage(whisperMsg);
                    System.out.println("[From "+nickName+" To "+toWhisper+"] "+ whisperMsg);

                    whisper(toWhisper, nickName, sendDto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(InfoDTO sendDto) throws IOException {
        for (ChatHandler handler : list) {
            handler.writer.writeObject(sendDto);
            handler.writer.flush();
        }
    }

    public void whisper(String toWhisper, String fromWhisper, InfoDTO sendDto) throws IOException {

        String msg = sendDto.getMessage();
        // nameList에서 toWhisper 찾기
        int i = 0;
        for (String name : nameList) {
            if (toWhisper.equals(name)) break;
            i++;
        }
        if (i < nameList.size()) {
            sendDto.setMessage("[From " + fromWhisper + "] " + msg);
            list.get(i).writer.writeObject(sendDto);
            list.get(i).writer.flush();
        } else {
            System.out.print("귓속말 대상이 없습니다.");
        }

        // nameList에서 fromWhisper 찾기
        int j = 0;
        for (String name : nameList) {
            if (fromWhisper.equals(name)) break;
            j++;
        }
        if (j < nameList.size()) {
            sendDto.setMessage("[To " + toWhisper + "] " + msg);
            list.get(j).writer.writeObject(sendDto);
            list.get(j).writer.flush();
        }

        // server에게 보내기
        if (!toWhisper.equals("server") && !fromWhisper.equals("server")) {
            sendDto.setMessage("[From "+fromWhisper+" To "+toWhisper+"] "+ msg);
            list.get(0).writer.writeObject(sendDto);
            list.get(0).writer.flush();
        }
    }
}
