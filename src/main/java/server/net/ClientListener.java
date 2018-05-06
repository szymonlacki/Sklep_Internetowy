package server.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;


import DAO.DAOFactory;
import DAO.DTO.User;
import DAO.InterfaceDAO;
import DAO.UserDAO;
import communication.REQUEST_ID;
import communication.RESPONSE_ID;
import communication.Request;
import communication.Response;
import server.WritableGUI;


public class ClientListener extends Thread {

    private Socket socket;
    private WritableGUI gui;



    ClientListener(Socket socket, WritableGUI gui) throws NullPointerException {
        this.gui = gui;
        this.socket = socket;
        if (socket == null)
            throw new NullPointerException("Null pointer in class constructor");

    }


    @Override
    public void run()
    {

        gui.write("TRANSFER STARTED");
        gui.write("STH started: Connected to: " + (socket.getInetAddress()).toString());

        try (

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()))
        {



            Request request = (Request) in.readObject();
            if (request.getId() == REQUEST_ID.TEST_CONNECTION) {

                gui.write("Klient " +socket.getInetAddress()+" chce sie polaczyc - " + request.getSql());

                while (!this.isInterrupted()) {
                    request = (Request) in.readObject();
                    switch (request.getId()) {
                        case TEST_CONNECTION:
                            gui.write("Odebrano polaczenie od klienta o tresci: " + request.getSql());
                            out.writeObject(new Response("HANDSHAKE SERVER"));
                            in.close();
                            out.close();
                            break;
                        case SELECT:
                            gui.write("Odebrano polaczenie (SELECT) od  klienta");
                            if(request.getUser() != null) gui.write(request.getUser().getLogin());
                            InterfaceDAO dao =  DAOFactory.getDAO(request.getSql());
                            List<?> DTOList = dao.SelectAll(request);
                            Response r = new Response("Odpowiedz : ", DTOList);
                            if(!r.getList().isEmpty()) r.setId(RESPONSE_ID.SUCCESS);
                            else r.setId(RESPONSE_ID.NO_DATA);
                            out.writeObject(r);
                            in.close();
                            out.close();
                            break;
                        case INSERT:
                            gui.write("Odebrano polaczenie (INSERT) od klienta o tresci: " + request.getSql());
                            //zapytanie
                            dao = DAOFactory.getDAO(request.getSql());
                            Response responseInsert = dao.Insert(request);
                            gui.write("wyslano do klienta "+ responseInsert.getId().toString());
                            out.writeObject(responseInsert);
                            in.close();
                            out.close();
                            break;
                        case DELETE:
                            gui.write("Odebrano polaczenie (DELETE) od klienta o tresci: " + request.getSql());
                            dao = DAOFactory.getDAO(request.getSql());
                            Response responseDelete = dao.Delete(request);
                            gui.write(request.getSql() +"\n" + responseDelete.getId().toString());
                            out.writeObject(responseDelete);
                            in.close();
                            out.close();
                            break;

                        case LOG_IN://wysyłamy w requescie Usera z wypełnionym tylko hasłem i loginem
                            gui.write("Odebrano polaczenie (LOG_IN) od  klienta");
                            dao =  DAOFactory.getDAO(request.getSql());
                            List<User> users =(List<User>) dao.SelectAll(request);
                            Response response= new Response();
                            response.setId(RESPONSE_ID.LOGIN_FAILED);
                            gui.write("Analizuje liczbe userow:"+users.size());
                            for (User user : users) {
                                if (user.getLogin().equals(request.getUser().getLogin()) &&
                                        user.getPassword().equals(request.getUser().getPassword())) {
                                    response.setUser(user);
                                    response.setId(RESPONSE_ID.LOGIN_SUCCESS);
                                    gui.write("wysyłam dene usera: " + response.getUser().getLogin());
                                    break;
                                }
                            }
                            out.writeObject(response);// otrzymujemy pełną informacje na temat usera jesli jest
                            in.close();
                            out.close();
                            break;
                        case REGISTER://wysyłamy w requescie pełne informacje o userze który ma sie zarejestrować
                            gui.write("Odebrano polaczenie (REGISTER) od  klienta");
                            dao = new UserDAO();
                            response= dao.Insert(request);
                            gui.write("Wysylam "+response.getId().toString());
                            out.writeObject(response);// otrzymujemy fail lub success
                            in.close();
                            out.close();
                            break;
                        case UPDATE://wysyłamy w requescie pełne informacje o userze który ma robic update
                            gui.write("Odebrano polaczenie (UPDATE) od  klienta");
                            dao = DAOFactory.getDAO(request.getSql());
                            response= dao.Update(request);
                            gui.write("Wysylam "+response.getId().toString());
                            out.writeObject(response);// otrzymujemy fail lub success
                            in.close();
                            out.close();
                            break;
                    }

                }

            }
        } catch (SocketException e) {
            gui.write("Transfer ZAKONCZONY");

            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        finally {
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("koniec klient listenera");
                }
            }
        }
    }
}
