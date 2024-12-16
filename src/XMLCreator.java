import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLCreator {

    public File createGameXML(int row, int col, String image1FileName, String image2FileName) {
        try {
            // XML Document 생성
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // <GameConfig> 루트 엘리먼트
            Element root = document.createElement("GameConfig");
            document.appendChild(root);

            // <Setting> 엘리먼트
            Element setting = document.createElement("Setting");
            setting.setAttribute("HowManyRows", String.valueOf(row));
            setting.setAttribute("HowManyCols", String.valueOf(col));
            root.appendChild(setting);

            // <Image> 엘리먼트
            Element image = document.createElement("Image");
            image.setAttribute("BlockImagePath_1", "./client_files/player1_" + image1FileName);
            image.setAttribute("BlockImagePath_2", "./client_files/player2_" + image2FileName);
            root.appendChild(image);

            // XML 파일로 저장
            File xmlFile = new File("./server_files/gameXML.xml");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(xmlFile);

            transformer.transform(domSource, streamResult);

            System.out.println("XML 파일 생성 완료: " + xmlFile.getAbsolutePath());
            return xmlFile;

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
