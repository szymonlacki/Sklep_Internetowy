package nasz.sklepik;

import DAO.DTO.Product;
import DAO.DTO.Purchase;
import DAO.DTO.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import communication.Protocol;
import communication.REQUEST_ID;
import communication.Request;
import communication.Response;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PdfCreator {

    private List<Purchase> purchaseList;
    private User buyer;
    private List<Product> productList;

    public PdfCreator(List<Purchase> purchaseList, User buyer) {
        this.purchaseList = purchaseList;
        this.buyer = buyer;

        Request r = new Request(REQUEST_ID.TEST_CONNECTION, "Handshake TESTClientTest");
        Request r2 = new Request(REQUEST_ID.SELECT, "PRODUCT");
        try {
            Socket socket = new Socket(Protocol.serverIP, Protocol.port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(r);
            out.writeObject(r2);
            Response response = (Response) in.readObject();
            this.productList = (List<Product>) response.getList();
            socket.close();
        } catch (Exception e) {
            System.out.println("Brak polaczenia z serverem!");
            // e.printStackTrace();
        }
    }


    private List<Chunk> preparePdfInformation()
    {
        List<Chunk> chunkList = new ArrayList<>();
        Font h1 = FontFactory.getFont(FontFactory.COURIER,18, BaseColor.BLACK);
        Font h2 = FontFactory.getFont(FontFactory.COURIER,12, BaseColor.BLACK);
        Font h3 = FontFactory.getFont(FontFactory.COURIER,8, BaseColor.BLACK);

        Double sumNetto = 0.00;
        Double sumBrutto = 0.00;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        chunkList.add(new Chunk("Faktura",h1));
        chunkList.add(new Chunk("Data wystawienia: "+ dateFormat.format(cal.getTime()),h2));
        chunkList.add(new Chunk("Nabywca: "+buyer.toPdfString(),h2));
        chunkList.add(new Chunk("Sprzedawca: "+" Najlepszy sklep internetowy w sieci sp z o.o",h2));
        chunkList.add(new Chunk(Chunk.NEWLINE));
        //dodawanie produktów do transakcji
        int i=1;
        for (Purchase purchase:purchaseList
             ) {
            for (Product product:productList
                 ) {
                 if(product.getId().equals(purchase.getProductId())){
                     Chunk chunk = new Chunk(i+"."+product.toPdfString()+" "+purchase.toPdfString(),h3);
                     sumNetto+=product.getPrice()/product.getVat();
                     sumBrutto+=product.getPrice();
                     chunkList.add(chunk);
                     i++;
                 }
            }
        }
        chunkList.add(new Chunk("Razem : Suma netto: "+ String.format("%.2fPLN", sumNetto) + " Suma Brutto:"+String.format("%.2fPLN", sumBrutto),h2));


        return chunkList;
    }

    public void createInvoice(){
        Document document = new Document();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wskaż plik pdf");
        fileChooser.setInitialFileName("faktura.pdf");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF (*.PDF, *.pdf)", "*.PDF", "*.pdf"));
        File path = fileChooser.showSaveDialog(Main.mainApp);
        if (path.toString().endsWith("pdf")) {
            try {
                PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(new File(path.getAbsolutePath())));
                document.open();
                List<Chunk> chunkList = preparePdfInformation();
                for (Chunk ch : chunkList
                        ) {
                    //document.add(ch);
                    document.add(new Paragraph(ch));
                    // document.add(Chunk.NEWLINE);
                }
                document.close();
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Wygenerowano pomyślnie!");
            alert2.showAndWait();
        }
    }
}
